package com.example.androidfinalgroupproject.covid19;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProvinceOpener extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "CountryDate";
    public final static String COL_COUNTRY = "COUNTRY";
    protected final static String DATABASE_NAME = "CovidDB";
    protected final static int VERSION_NUM = 1;
    public final static String COL_PROVINCE = "PROVINCE";
    public final static String COL_DATE = "DATE";
    public final static String COL_ID = "_ID";
    public final static String COL_COUNTRY_CODE = "COUNTRY_CODE";
    public final static String COL_LATITUDE = "LATITUDE";
    public final static String COL_LONGGITUDE = "LONGITUDE";
    public final static String COL_CASE_NUMBER = "CASE_NUMBER";


    public ProvinceOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_COUNTRY + " TEXT,"
                + COL_COUNTRY_CODE + " TEXT,"
                + COL_LATITUDE + " TEXT,"
                + COL_LONGGITUDE + " TEXT,"
                + COL_PROVINCE + " TEXT,"
                + COL_CASE_NUMBER + " TEXT,"
                + COL_DATE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + "New version:" + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:

        Log.i("Database downgrade", "Old version:" + oldVersion + "New version:" + newVersion);
        //drop old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //build new table
        onCreate(db);
    }

}

