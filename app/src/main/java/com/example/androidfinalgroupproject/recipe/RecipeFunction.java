package com.example.androidfinalgroupproject.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/05 
 * @Version: 1.0
 */

//functions related to recipe search
public class RecipeFunction {
    private SQLiteDatabase recipeSQLiteDatabase;
    private RecipeOpener rOpener;
    String[] allColumns;

    {
        assert recipeSQLiteDatabase != null;
        allColumns = new String[]{recipeSQLiteDatabase.TITLE, recipeSQLiteDatabase.INGREDIENT, recipeSQLiteDatabase.URL};
    }

    public RecipeFunction(Context context) {
        rOpener = new recipeSQLiteDatabase(context);
    }

    public void open() {
        recipeSQLiteDatabase = rOpener.getWritableDatabase();
    }

    //add recipe to favourite 
    public void addToFavoriteList(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(recipeSQLiteDatabase.TITLE, recipe.getTitle());
        values.put(recipeSQLiteDatabase.INGREDIENT, recipe.getIngredient());
        values.put(recipeSQLiteDatabase.URL, recipe.getUrl());
        recipeSQLiteDatabase.insert(recipeSQLiteDatabase.TABLE_NAME, null, values);
    }
    //cursor movement to select recipe
    private Recipe cursorTorecipe(Cursor cursor) {


        Recipe r = new Recipe();
        r.setTitle(cursor.getString(0));
        r.setIngredient(cursor.getString(1));
        r.setUrl(cursor.getString(2));
        return r;
    }
    //remove selected recipe from favourited table
    public void deleteSelectedRecipe(Recipe r) {
        String recipeselected = r.getUrl();
        recipeSQLiteDatabase.delete(recipeSQLiteDatabase.TABLE_NAME, recipeSQLiteDatabase.TITLE + "=" + "'" + recipeselected + "'", null);
    }

    public List<Recipe> getAllFavoriteRecipe() {
        List<Recipe> recipelist = new ArrayList<>();
        Cursor cursor = recipeSQLiteDatabase.query(recipeSQLiteDatabase.TABLE_NAME, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe newrecipe = cursorTorecipe(cursor);
            recipelist.add(newrecipe);
            cursor.moveToNext();
        }
        cursor.close();
        return recipelist;
    }

    public boolean isRecipeNotExists(Recipe r) {
        Cursor cursor = recipeSQLiteDatabase.query(recipeSQLiteDatabase.TABLE_NAME, allColumns,
                recipeSQLiteDatabase.TITLE + "=" + "'" + r.getTitle() + "'",
                null, null, null, null);
        boolean result = (cursor.getCount() == 0);
        cursor.close();
        return result;
    }
}

