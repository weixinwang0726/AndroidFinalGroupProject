package com.example.androidfinalgroupproject.audio.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Author: Yanan Cheng
 * @Date: 2020/11/19 11:12
 * @Version: 1.0
 */
public class AlbumSQLiteOpener extends SQLiteOpenHelper {
    static final String TABLE_NAME = "favorite_album";
    static final String ARTIST = "artist";
    static final String ALBUM = "album";
    static final String ALBUM_ID = "album_id";
    static final String DATABASE_NAME = "favorite_album.db";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_NAME + "( " + ARTIST + " TEXT, " + ALBUM + " TEXT, "
            + ALBUM_ID + " TEXT PRIMARY KEY" + ");";

    public AlbumSQLiteOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
