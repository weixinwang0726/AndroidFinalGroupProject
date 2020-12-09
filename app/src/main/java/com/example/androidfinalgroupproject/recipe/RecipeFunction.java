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
        //assert recipeSQLiteDatabase != null;
        //define column names of the SQLiteDatabase
        allColumns = new String[]{RecipeOpener.TITLE, RecipeOpener.INGREDIENT, RecipeOpener.URL};
    }

    public RecipeFunction(Context context) {
        rOpener = new RecipeOpener(context);
    }

    public void open() {
        recipeSQLiteDatabase = rOpener.getWritableDatabase();
    }

    //add recipe to favourite 
    public void addToFavoriteList(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeOpener.TITLE, recipe.getTitle());
        values.put(RecipeOpener.INGREDIENT, recipe.getIngredient());
        values.put(RecipeOpener.URL, recipe.getUrl());
        //insert the content to database
        recipeSQLiteDatabase.insert(RecipeOpener.TABLE_NAME, null, values);
    }

    public boolean isRecipeNotExists(Recipe r) {
        Cursor cursor = recipeSQLiteDatabase.query(RecipeOpener.TABLE_NAME, allColumns,
                RecipeOpener.TITLE + "=" + "'" + r.getTitle() + "'",
                null, null, null, null);
        boolean result = (cursor.getCount() == 0);
        cursor.close();
        return result;
    }


    //remove selected recipe from favourite table
    public void deleteSelectedRecipe(Recipe r) {
        String recipeselected = r.getUrl();
        recipeSQLiteDatabase.delete(RecipeOpener.TABLE_NAME, RecipeOpener.TITLE + "=" + "'" + recipeselected + "'", null);
    }

    //get all saved recipes to a list
    public List<Recipe> getAllFavoriteRecipe() {
        List<Recipe> recipelist = new ArrayList<>();
        Cursor cursor = recipeSQLiteDatabase.query(RecipeOpener.TABLE_NAME, allColumns,
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


    private Recipe cursorTorecipe(Cursor cursor) {

        Recipe r = new Recipe();
        r.setTitle(cursor.getString(0));
        r.setIngredient(cursor.getString(1));
        r.setUrl(cursor.getString(2));
        return r;
    }


}