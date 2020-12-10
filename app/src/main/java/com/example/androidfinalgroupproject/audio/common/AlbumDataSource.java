package com.example.androidfinalgroupproject.audio.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Yanan Cheng
 * @Date: 2020/11/19 12:16
 * @Version: 1.0
 * Album data source
 */
public class AlbumDataSource {
    private SQLiteDatabase mSQLiteDatabase;
    private AlbumSQLiteOpener mSQLiteOpener;
    String[] allColumns = {AlbumSQLiteOpener.ARTIST, AlbumSQLiteOpener.ALBUM, AlbumSQLiteOpener.ALBUM_ID};

    public AlbumDataSource(Context context) {
        mSQLiteOpener = new AlbumSQLiteOpener(context);
    }

    public void open() {
        mSQLiteDatabase = mSQLiteOpener.getWritableDatabase();
    }

    public void addToFavoriteList(Album album) {
        ContentValues values = new ContentValues();
        values.put(AlbumSQLiteOpener.ARTIST, album.getArtist());
        values.put(AlbumSQLiteOpener.ALBUM, album.getStrAlbum());
        values.put(AlbumSQLiteOpener.ALBUM_ID, album.getIdAlbum());
        mSQLiteDatabase.insert(AlbumSQLiteOpener.TABLE_NAME, null, values);
    }

    private Album cursorToAlbum(Cursor cursor) {
        Album album = new Album();
        album.setArtist(cursor.getString(0));
        album.setStrAlbum(cursor.getString(1));
        album.setIdAlbum(cursor.getString(2));
        return album;
    }

    public void deleteSelectedAlbum(Album album) {
        String albumContent = album.getIdAlbum();
        mSQLiteDatabase.delete(AlbumSQLiteOpener.TABLE_NAME, AlbumSQLiteOpener.ALBUM_ID + "=" + "'" + albumContent + "'", null);
    }

    public List<Album> getAllFavoriteAlbums() {
        List<Album> albums = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(AlbumSQLiteOpener.TABLE_NAME, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Album Album = cursorToAlbum(cursor);
            albums.add(Album);
            cursor.moveToNext();
        }
        cursor.close();
        return albums;
    }

    public boolean isAlbumNotExists(Album album) {
        Cursor cursor = mSQLiteDatabase.query(AlbumSQLiteOpener.TABLE_NAME, allColumns,
                AlbumSQLiteOpener.ALBUM_ID + "=" + "'" + album.getIdAlbum() + "'",
                null, null, null, null);
        boolean result = (cursor.getCount() == 0);
        cursor.close();
        return result;
    }
}
