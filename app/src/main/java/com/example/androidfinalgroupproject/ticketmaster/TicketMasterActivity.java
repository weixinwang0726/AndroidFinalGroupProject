package com.example.androidfinalgroupproject.ticketmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalgroupproject.MainActivity;
import com.example.androidfinalgroupproject.R;
import com.example.androidfinalgroupproject.audio.AudioMainActivity;
import com.example.androidfinalgroupproject.common.Const;
import com.example.androidfinalgroupproject.covid19.Covid19Case;
import com.example.androidfinalgroupproject.recipe.RecipeActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Ticket Master Activity.
 */
public class TicketMasterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences prefs;
    private EventsAdapter citiesAdapter;
    ProgressBar progressBar;
    private List<Event> eventList = new ArrayList<>();
    private List<Event> favoriteList = new ArrayList<>();

    private HashMap<String,Event> favotatedMap = new HashMap<String,Event>();

    Intent gotoEventDetail;
    Event e = new Event();
    EventOpener eventOpener;
    SQLiteDatabase db;
    private Helper tmHelper = new Helper();
    DetailsFragment dFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //hello.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tm);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.mainBar);
        setSupportActionBar(myToolbar);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolbar, R.string.open, R.string.close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        boolean isTablet =    findViewById(R.id.fragmentLocation) != null;
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        prefs = getSharedPreferences(Const.PREF_PRAMS_DBNAME, Context.MODE_PRIVATE);
        String preCity = prefs.getString(Const.PREF_TM_CITY, "");
        String preRadius = prefs.getString(Const.PREF_TM_RADIUS, "");

        EditText cityInput = findViewById(R.id.tm_city_input);
        EditText radiusInput = findViewById(R.id.tm_radius_input);

        cityInput.setText(preCity);
        radiusInput.setText(preRadius);

        ListView cityListView = findViewById(R.id.event_list_view);
        cityListView.setAdapter(citiesAdapter = new EventsAdapter());

        TextView helpBtn = navigationView.getHeaderView(0).findViewById(R.id.help_btn);
        helpBtn.setOnClickListener(click -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(navigationView.getContext());

            alertDialogBuilder.setTitle(R.string.tm_help_btn)
                    .setMessage(R.string.tm_help_content)
                    .setPositiveButton(R.string.tm_yes, (c, arg) -> {
                    })
                    .create().show();
        });
        progressBar = findViewById(R.id.progress_horizontal);
        loadEventList("", "radius",true);
        eventList.clear();
        for(Event e: favoriteList){
            favotatedMap.put(e.getEventId(),e);
        }

        //gotoFavoriteList = new Intent(TicketMasterActivity.this, FavoriteCitiesActivity.class);
        Button gotoFavoriteBtn = findViewById(R.id.favorite_btn);
        gotoFavoriteBtn.setOnClickListener(click -> {
            if(isTablet){
                if(null != dFragment)
                getSupportFragmentManager().beginTransaction().remove(dFragment).commit();
            }
            //startActivity(gotoFavoriteList);
            loadEventList("", "radius",true);
        });

        gotoEventDetail = new Intent(TicketMasterActivity.this, EventDetailActivity.class);

        Button searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(click -> {
            if(isTablet){
                if(null != dFragment)
                    getSupportFragmentManager().beginTransaction().remove(dFragment).commit();
            }
                String city = cityInput.getText().toString();
                String radius = radiusInput.getText().toString();
                tmHelper.saveArguments(prefs, city, radius);
                loadEventList(city, radius, false);


        });

        //Click the event's name and see the detail of the event.
        cityListView.setOnItemClickListener((parent, view, position, id) -> {
                    Event event = eventList.get(position);
                    dFragment = new DetailsFragment();
                    Bundle dataToPass = new Bundle();

                    if(isTablet)
                    {
                        tmHelper.saveEventToBundle(dataToPass,event);
                        //cityListView.setVisibility(View.INVISIBLE);
                        //add a DetailFragment
                        dFragment.setArguments( dataToPass ); //pass it a bundle for information
                        //  dFragment.setTablet(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                                //   .addToBackStack("AnyName")
                                .commit(); //actually load the fragment.
                    }else {
                        tmHelper.saveEventToExtra(gotoEventDetail, event);
                        startActivity(gotoEventDetail);
                    }
        });
    }

    /**
     * load event. if isfavoritedList is false maens to fetch data from the website.
     * if is favoritedlist is true, just fetch data from the DB.
     * @param cityName
     * @param radius
     * @param isFavoritedList
     */
    private void loadEventList(String cityName, String radius,boolean isFavoritedList) {
        if(!isFavoritedList) {
            eventList.clear();
            progressBar.setVisibility(View.VISIBLE);
            EventsQuery eventsQuery = new EventsQuery();
            //https://app.ticketmaster.com/discovery/v2/events.json?size=1&apikey=c9ICqSyBDxp3Q0ItYjjkKmeNQtYXFw9j
            String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=c9ICqSyBDxp3Q0ItYjjkKmeNQtYXFw9j&city=" + cityName + "&radius=" + radius;

            Log.e("Request url", url);
            eventsQuery.execute(url);

            favoriteList = getFavoriteEvents();
            favotatedMap.clear();
            for(Event e: favoriteList){
                favotatedMap.put(e.getEventId(),e);
            }
        }else{
            eventList.clear();
            progressBar.setVisibility(View.VISIBLE);

            favoriteList = getFavoriteEvents();

            eventList.addAll(favoriteList);
            if (eventList.size() == 0) {
                Toast.makeText(TicketMasterActivity.this, "No events nearby", Toast.LENGTH_LONG).show();
            }
            citiesAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class EventsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public Object getItem(int position) {
            return eventList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return eventList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = (Event) getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            //make a new rowrow_city_layout
            View rowView = inflater.inflate(R.layout.activity_tm_row_data_layout, parent, false);

            TextView nameView = rowView.findViewById(R.id.tm_text_view);
            ImageView imageView = rowView.findViewById(R.id.tm_image_star);
            ImageButton deleteButton = rowView.findViewById(R.id.tm_delete_btn);
            if(favotatedMap.containsKey(event.getEventId())){
                imageView.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
            }

            nameView.setText(event.getName());
//            nameView.setOnClickListener(click->{
//                tmHelper.saveEventToExtra(gotoEventDetail,event);
//                startActivity(gotoEventDetail); }
//
//            );
            deleteButton.setOnClickListener(click->{
                db.delete(Const.TABLE_NAME_TM,EventOpener.COL_EVENT_ID+" =? ",new String[]{event.getEventId()});

                eventList.remove(event);
                citiesAdapter.notifyDataSetChanged();
            });
            return rowView;
        }
    }


    /**
     * async task class.
     */
    private class EventsQuery extends AsyncTask<String, Integer, String> {

        private List<Event> eventResults = new ArrayList();

        /**
         * execute background.
         * @param args
         * @return
         */
        @Override
        protected String doInBackground(String... args) {

            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                eventResults.clear();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                JSONObject jsonObject = new JSONObject(result);

                JSONArray eventsJSONArray = null;
                JSONObject eventJSONObject = null;

                jsonObject = jsonObject.getJSONObject("_embedded");
                eventsJSONArray = jsonObject.getJSONArray("events");
                jsonObject.toJSONArray(eventsJSONArray);

                String eventName = "";
                HashMap<String,String> eventNames = new HashMap<String,String>();
                for (int i = 0; i < eventsJSONArray.length(); i++) {
                    Event event = new Event();
                    eventJSONObject = eventsJSONArray.getJSONObject(i);
                    event.setEventId(eventJSONObject.getString("id"));
                    eventName = eventJSONObject.getString("name");
                    if(eventNames.containsKey(eventName)){
                        continue;
                    }else{
                        eventNames.put(eventName,"");
                    }
                    event.setName(eventJSONObject.getString("name"));
                    Log.i("xml data",eventJSONObject.getString("name"));
                    Log.i("xml data",eventJSONObject.getString("name"));
                    event.setId(i);
                    event.setEventId(eventJSONObject.getString("id"));
                    event.setStartDate(eventJSONObject.getJSONObject("dates").getJSONObject("start").getString("localDate"));
                    event.setPriceMin(eventJSONObject.getJSONArray("priceRanges").getJSONObject(0).getDouble("min"));
                    event.setPriceMax(eventJSONObject.getJSONArray("priceRanges").getJSONObject(0).getDouble("max"));
                    event.setUrl(eventJSONObject.getString("url"));
                    event.setImageUrl(eventJSONObject.getJSONArray("images").getJSONObject(0).getString("url"));

                    Log.i("Event info", event.toString());

                    this.eventResults.add(event);
                    publishProgress(i * (100 / eventsJSONArray.length()));
                    progressBar.setProgress(i * (100 / eventsJSONArray.length()));
                }
                publishProgress(100);
                progressBar.setProgress(100);

            } catch (Exception e) {
                String msg = e.getMessage();
                Log.e("Search events", e.getMessage());

            }
            return "Search Done";
        }

        /**
         * do something after the async method.
         * @param fromDoInBackground
         */
        public void onPostExecute(String fromDoInBackground) {
            progressBar.setVisibility(View.INVISIBLE);
            eventList.clear();
            eventList.addAll(this.eventResults);
            if (eventList.size() == 0) {
                Toast.makeText(TicketMasterActivity.this, "No events nearby", Toast.LENGTH_LONG).show();
            }
            citiesAdapter.notifyDataSetChanged();
        }
    }

    /**
     * get all favorite events.
     * @return
     */
    private ArrayList<Event> getFavoriteEvents() {
        eventOpener = new EventOpener(this);
        db = eventOpener.getWritableDatabase();

        Cursor favoCursor = db.query(false, Const.TABLE_NAME_TM, eventOpener.columns, null, null, null, null, null, null);
        ArrayList<Event> favoEventList = eventOpener.printCursor(favoCursor);
        return favoEventList;
    }

    /**
     * this is the left menu bar.
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switchBetweenActivity(item.getItemId());
        return false;
    }
    public void switchBetweenActivity(int id) {
        switch (id){
            case R.id.audio_search:
                startActivity(new Intent(TicketMasterActivity.this, AudioMainActivity.class));
                break;
            case R.id.ticket_master:
                startActivity(new Intent(TicketMasterActivity.this, TicketMasterActivity.class));
                break;
            case R.id.covid19:
                startActivity(new Intent(TicketMasterActivity.this, Covid19Case.class));
                break;

            case R.id.recipe:
                startActivity(new Intent(TicketMasterActivity.this, RecipeActivity.class));
                break;
        }
    }

}