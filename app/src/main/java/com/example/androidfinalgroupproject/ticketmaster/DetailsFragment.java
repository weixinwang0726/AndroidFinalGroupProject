package com.example.androidfinalgroupproject.ticketmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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


public class DetailsFragment extends Fragment {

    private Bundle bundle;
    private long id;
    private AppCompatActivity parentActivity;
    SQLiteDatabase db;
    Button addToFavoriteBtn;
    Button removeFromFavoriteBtn;
    Event e = new Event();
    EventOpener eventOpener;
    ProgressBar eventProgressBar;
    ImageView eventImage;
    //  private long dbID;
//    public DetailFragment() {
//        // Required empty public constructor
//    }
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        bundle = getArguments();
        //  id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );
        View result =  inflater.inflate(R.layout.activity_tm_fragment_details, container, false);

        addToFavoriteBtn = result.findViewById(R.id.tm_add_favorite_btn);
        removeFromFavoriteBtn = result.findViewById(R.id.tm_remove_from_btn);

        eventProgressBar = (ProgressBar) result.findViewById(R.id.progressBar);
        eventProgressBar.setVisibility(View.VISIBLE);


        TextView eventName = (TextView)result.findViewById(R.id.tm_event_name);
        eventName.setText(bundle.getString(Const.PARAMS_TM_NAME));

        TextView startDate = (TextView)result.findViewById(R.id.tm_event_start_date);
        startDate.setText(bundle.getString(Const.PARAMS_TM_START_DATE));

        TextView priceMin = (TextView)result.findViewById(R.id.tm_tw_price_min);
        priceMin.setText(""+bundle.getDouble(Const.PARAMS_TM_PRICE_MIN));

        TextView priceMax = (TextView)result.findViewById(R.id.tm_tw_price_max);
        priceMax.setText(""+bundle.getDouble(Const.PARAMS_TM_PRICE_MAX));

        TextView url = (TextView)result.findViewById(R.id.tm_tw_url);
        url.setText(bundle.getString(Const.PARAMS_TM_URL));

        //ImageView imangeUrl = (ImageView)result.findViewById(R.id.tm_tw_image_url);
        eventImage = result.findViewById(R.id.tm_tw_image_url);
        //imangeUrl.setText(bundle.getString(Const.PARAMS_TM_IMAGE_URL));



        e.setEventId(bundle.getString(Const.PARAMS_TM_EVENT_ID));
        e.setName(bundle.getString(Const.PARAMS_TM_NAME));
        e.setStartDate(bundle.getString(Const.PARAMS_TM_START_DATE));
        e.setPriceMin(bundle.getDouble(Const.PARAMS_TM_PRICE_MIN));
        e.setPriceMax(bundle.getDouble(Const.PARAMS_TM_PRICE_MAX));
        e.setUrl(bundle.getString(Const.PARAMS_TM_URL));
        e.setImageUrl(bundle.getString(Const.PARAMS_TM_IMAGE_URL));

        String iconFileName = e.getImageUrl().substring(e.getImageUrl().lastIndexOf("/")+1);
        ForecastQuery query = new ForecastQuery();
        query.execute(e.getImageUrl(),iconFileName);

        Button hideButton = (Button)result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            //        ChatRoomActivity parent = (ChatRoomActivity) getActivity();
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        determineWhichBtnShow();
        addToFavoriteBtn.setOnClickListener(click -> {
            //if(!ifFavoriteCity(e)) {
                addToFavoriteList();
                Snackbar.make(click, getString(R.string.tm_snack_message_add), BaseTransientBottomBar.LENGTH_LONG)
                        .setAction(getString(R.string.cancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeFromFavoriteList();
                                Snackbar.make(v, getString(R.string.cancelled), BaseTransientBottomBar.LENGTH_LONG).show();
                            }
                        }).show();

            //}
        });

        removeFromFavoriteBtn.setOnClickListener(click -> {
            //if(ifFavoriteCity(e)) {
                removeFromFavoriteList();
                Snackbar.make(click, getString(R.string.tm_snack_message_add), BaseTransientBottomBar.LENGTH_LONG)
                        .setAction(getString(R.string.cancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToFavoriteList();
                                Snackbar.make(v, getString(R.string.cancelled), BaseTransientBottomBar.LENGTH_LONG).show();
                            }
                        }).show();
            //}
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
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
     *
     * @param e
     * @return
     */
    private boolean ifFavoriteCity(Event e) {
        eventOpener = new EventOpener(parentActivity);
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
                        fis = parentActivity.openFileInput(iconFileName);
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
                    FileOutputStream outputStream = parentActivity.openFileOutput(iconName, Context.MODE_PRIVATE);

                    boolean saveImage = image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                }
            } catch (Exception e) {
                Log.i("image error:",e.toString());
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
    /**
     * check the file exists or not.
     * @param fname
     * @return
     */
    private boolean fileExistance(String fname){
        File file = parentActivity.getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

}