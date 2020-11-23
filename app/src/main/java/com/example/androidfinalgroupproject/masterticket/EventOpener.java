package com.example.androidfinalgroupproject.masterticket;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.androidfinalgroupproject.common.Const;

import java.util.ArrayList;

/**
 * Operator for the DB.
 */
public class EventOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "EventDB";
    protected final static int VERSION_NUM = 1;

    public final static String COL_NAME = "name";
    public final static String COL_START_DATE = "start_date";
    public final static String COL_PRICE_MIN = "price_min";
    public final static String COL_PRICE_MAX = "price_max";
    public final static String COL_URL = "url";
    public final static String COL_IMAGE_URL = "image_url";
    public final static String COL_EVENT_ID  = "event_id";
    public final static String COL_ID = "_id";

    public final static String[] columns = {EventOpener.COL_ID, EventOpener.COL_START_DATE, EventOpener.COL_PRICE_MIN,
            EventOpener.COL_PRICE_MAX, EventOpener.COL_URL, EventOpener.COL_IMAGE_URL,
            EventOpener.COL_EVENT_ID,EventOpener.COL_NAME};
    public EventOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + Const.TABLE_NAME_TM + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_NAME + " text,"
                    + COL_START_DATE + " text,"
                    + COL_PRICE_MIN + " text,"
                    + COL_PRICE_MAX + " text,"
                    + COL_URL + " text,"
                    + COL_IMAGE_URL + " text,"
                    + COL_EVENT_ID + " text);");
        }

    /**
     * print the result.
     * @param results
     * @return
     */
    public ArrayList<Event> printCursor(Cursor results)
    {

        String [] columns = results.getColumnNames();
        ArrayList<Event> itemList = getEventItems(results);

        for(String column:columns){
            Log.d("cursor info:","column name is:"+column);
        }
        Log.d("cursor info","number of rows is:"+itemList.size());
        for(Event msgItem:itemList){
            Log.d("cursor info",msgItem.toString());
        }

        return itemList;
        //At this point, the contactsList array has loaded every row from the cursor.
    }

    /**
     * fetch the data.
     * @param results
     * @return
     */
    public ArrayList<Event> getEventItems(Cursor results) {

        String [] columns = results.getColumnNames();

        ArrayList<Event> msgList = new ArrayList<Event>();
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            Event e = new Event();
            e.setId(results.getLong(results.getColumnIndex(columns[0])));
            e.setStartDate(results.getString(results.getColumnIndex(columns[1])));
            e.setPriceMin(results.getLong(results.getColumnIndex(columns[2])));
            e.setPriceMax(results.getLong(results.getColumnIndex(columns[3])));
            e.setUrl(results.getString(results.getColumnIndex(columns[4])));
            e.setImageUrl(results.getString(results.getColumnIndex(columns[5])));
            e.setEventId(results.getString(results.getColumnIndex(columns[6])));
            e.setName(results.getString(results.getColumnIndex(columns[7])));
            msgList.add(e);
        }
        return msgList;
    }

    /**
     * delete the old table and recreate a new table.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_NAME_TM);

        //Create the new table:
        onCreate(db);
    }

    /**
     * drop and create.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_NAME_TM);

        //Create the new table:
        onCreate(db);
    }



}
