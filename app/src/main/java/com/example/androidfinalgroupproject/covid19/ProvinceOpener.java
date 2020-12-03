package com.example.androidfinalgroupproject.covid19;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProvinceOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "CovidDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "CountryDate";
    public final static String COL_COUNTRY = "COUNTRY";
    public final static String COL_DATE = "DATE";
    public final static String COL_PROVINCE = "PROVINCE";
    public final static String COL_CASE = "COVIDCASE";
    public final static String COL_ID = "_ID";

    /*
        constructor
     */
    public ProvinceOpener(Context ctx) {

        super(ctx, DATABASE_NAME, null, VERSION_NUM);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_COUNTRY + " TEXT,"
                + COL_PROVINCE + " TEXT,"
                + COL_CASE + " TEXT,"
                + COL_DATE + " TEXT);");

//        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_COUNTRY + " text,"
//                + COL_REGION + " text,"
//                + COL_CITY + " text,"
//                + COL_LATITUDE + " text,"
//                + COL_LONGITUDE + " text,"
//                + COL_CURRENCY_NAME + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // write information to log
        Log.i("Database upgrade", "Old version:" + oldVersion + "New version:" + newVersion);
        //drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //Create the new table
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        // write information to log
        Log.i("Database downgrade", "Old version:" + oldVersion + "New version:" + newVersion);
        //drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //Create the new table
        onCreate(db);
    }

}

