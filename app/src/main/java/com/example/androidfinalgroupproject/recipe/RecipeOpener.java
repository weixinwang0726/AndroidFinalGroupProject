package com.example.androidfinalgroupproject.recipe;
/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/03
 * @Version: 1.0
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//recipe results added and saved to favourite, store to a table
//if the user select one of the recipes to save, show the details of the recipe with title, ingredient and url
public class RecipeOpener extends SQLiteOpenHelper {
    //table name
    static final String TABLE_NAME = "favorite_recipe";
    //column names
    static final String TITLE = "title";
    static final String INGREDIENT = "ingredient";
    static final String URL = "url";
    //database name
    static final String DATABASE_NAME = "favorite_recipe.db";
    //database version
    static final int DATABASE_VERSION = 1;
    //create table
    static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_NAME + "( " + TITLE + " TEXT, " + INGREDIENT + " TEXT, "
            + URL + " TEXT PRIMARY KEY" + ");";  //store the details of the recipe with title, ingredient and url


    public RecipeOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This  method is to create a recipe database
     *
     * @param db SQLiteDatabase object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }


    /**
     * This method is to upgrade database version
     *
     * @param db         SQLiteDatabase object
     * @param oldVersion oldverison
     * @param newVersion newverison
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}