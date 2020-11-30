package com.example.androidfinalgroupproject.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalgroupproject.R;
import com.example.androidfinalgroupproject.common.Const;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Event's detail's page.
 */
public class EventDetailActivity extends AppCompatActivity {

    Button addToFavoriteBtn;
    Button removeFromFavoriteBtn;

    long id;
    Event e = new Event();
    EventOpener eventOpener;
    SQLiteDatabase db;
    ImageView eventImage;
    ProgressBar eventProgressBar;
    Helper tmHelper = new Helper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tm_event_detail);

        Intent fromIntent = getIntent();

        addToFavoriteBtn = findViewById(R.id.tm_add_favorite_btn);
        removeFromFavoriteBtn = findViewById(R.id.tm_remove_from_btn);

        eventProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        eventProgressBar.setVisibility(View.VISIBLE);

        //id = fromIntent.getLongExtra("id", -1);

        e = tmHelper.initiateEventWithExtra(e,fromIntent);

        //get items and set values.
        TextView eventTextView      = findViewById(R.id.tm_event_name);
        TextView startDateTextView  = findViewById(R.id.tm_event_start_date);
        TextView priceMinTextView   = findViewById(R.id.tm_tw_price_min);
        TextView priceMaxTextView   = findViewById(R.id.tm_tw_price_max);
        TextView urlTextView        = findViewById(R.id.tm_tw_url);
        eventImage = findViewById(R.id.tm_tw_image_url);

        eventTextView.setText(e.getName());
        startDateTextView.setText(e.getStartDate());
        priceMinTextView.setText(""+e.getPriceMin());
        priceMaxTextView.setText(""+e.getPriceMax());
        urlTextView.setText(e.getUrl());
        //imageUrlTextView.setText(e.getImageUrl());
        String iconFileName = e.getImageUrl().substring(e.getImageUrl().lastIndexOf("/")+1);

        ForecastQuery query = new ForecastQuery();
        query.execute(e.getImageUrl(),iconFileName);

        determineWhichBtnShow();

        addToFavoriteBtn.setOnClickListener(click -> {
            addToFavoriteList();
            Snackbar.make(click, e.getName() + getString(R.string.snackbar_message), BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(getString(R.string.cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeFromFavoriteList();
                            Snackbar.make(v, getString(R.string.cancelled), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    }).show();


        });

        removeFromFavoriteBtn.setOnClickListener(click -> {
            removeFromFavoriteList();
            Snackbar.make(click, getString(R.string.delete_message), BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(getString(R.string.cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToFavoriteList();
                            Snackbar.make(v, getString(R.string.cancelled), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    }).show();
        });
    }

    /**
     * check the file exists or not.
     * @param fname
     * @return
     */
    private boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    /**
     *
     * @param e
     * @return
     */
    private boolean ifFavoriteCity(Event e) {
        eventOpener = new EventOpener(this);
        db = eventOpener.getWritableDatabase();
        //eventOpener.onDowngrade(db,1,1);
        //eventOpener.onCreate(db);

        String selection = EventOpener.COL_EVENT_ID + " =? ";
        String[] selectionArgs = {e.getEventId()};

        //Cursor results = db.query(false, EventOpener.TABLE_NAME, columns, null, selectionArgs, null, null, null, null);
        Cursor results = db.query(false, Const.TABLE_NAME_TM, eventOpener.columns, selection, selectionArgs, null, null, null, null);

        return results.getCount() > 0;
    }

    /**
     * control the buttons to display or disabled.
     */
    private void determineWhichBtnShow() {
        if (ifFavoriteCity(e)) {
            addToFavoriteBtn.setVisibility(View.INVISIBLE);
            removeFromFavoriteBtn.setVisibility(View.VISIBLE);
        } else {
            addToFavoriteBtn.setVisibility(View.VISIBLE);
            removeFromFavoriteBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * save the event to db.
     */
    private void addToFavoriteList() {
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(EventOpener.COL_NAME,          e.getName());
        newRowValues.put(EventOpener.COL_START_DATE,    e.getStartDate());
        newRowValues.put(EventOpener.COL_PRICE_MIN,     e.getPriceMin());
        newRowValues.put(EventOpener.COL_PRICE_MAX,     e.getPriceMax());
        newRowValues.put(EventOpener.COL_URL,           e.getUrl());
        newRowValues.put(EventOpener.COL_IMAGE_URL,     e.getImageUrl());
        newRowValues.put(EventOpener.COL_EVENT_ID,      e.getEventId());

        long newId = db.insert(Const.TABLE_NAME_TM, null, newRowValues);
        this.id = newId;
        //Toast.makeText(EventDetailActivity.this, "Successfully added!", Toast.LENGTH_LONG).show();
        determineWhichBtnShow();
    }

    /**
     * delete the event from db.
     */
    private void removeFromFavoriteList() {
        String whereClause =EventOpener.COL_EVENT_ID + " =? ";
        db.delete(Const.TABLE_NAME_TM, whereClause, new String[]{e.getEventId()});
        //Toast.makeText(EventDetailActivity.this, "Successfully removed!", Toast.LENGTH_LONG).show();
        determineWhichBtnShow();
    }

    /**
     * async class and method to fetch the json data, and transfer data to Event data.
     */
    private  class ForecastQuery extends AsyncTask<String, Integer, String> {
        Bitmap image;

        @Override
        protected String doInBackground(String... args) {

            String imageUrl = args[0];
            String iconName = args[1];
            try {

                //create a URL object of what server to contact:
                URL url = new URL(imageUrl);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                String iconFileName = iconName;

                if (fileExistance(iconFileName)) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(iconFileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    image = BitmapFactory.decodeStream(fis);
                    Log.i("WeatherForecast", "Image already exists");

                } else {
                    //  Bitmap image = null;
                    URL iconUrl = new URL(imageUrl);

                    HttpURLConnection connection = (HttpURLConnection) iconUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                    FileOutputStream outputStream = openFileOutput(iconName, Context.MODE_PRIVATE);

                    boolean saveImage = image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                }
            } catch (Exception e) {
            }
            return "Done";
        }

        /**
         * update the percent of the process.
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            eventProgressBar.setProgress(values[0]);
            eventProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * execute this code just after finishing the async method.
         * @param results
         */
        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            try {
                eventImage.setImageBitmap(image);
                Log.e("image process", "image loaded.");
                // weatherProgressBar.setVisibility(View.INVISIBLE);
            }catch(Exception e) {
                Log.e("Exception.",e.toString());
            }
            eventProgressBar.setVisibility(View.INVISIBLE);
        }
    }

}