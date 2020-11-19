package com.example.androidfinalgroupproject.masterticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalgroupproject.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TicketMasterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SharedPreferences prefs;
    private CityListAdapter cityListAdapter;
    ProgressBar progressBar;
    private List<City> cityList = new ArrayList<>();
    Intent gotoFavoriteList;
    Intent gotoCityDetail;
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null); //this line avoids the icons to appear shaded gray. src: https://stackoverflow.com/questions/31394265/navigation-drawer-item-icon-not-showing-original-colour
        navigationView.setNavigationItemSelectedListener(this);


        prefs = getSharedPreferences("geoArguments", Context.MODE_PRIVATE);
        String latitude = prefs.getString("latitude", "");
        String longitude = prefs.getString("longitude", "");

        EditText latitudeInput = findViewById(R.id.latitude_input);
        EditText longitudeInput = findViewById(R.id.longitude_input);

        latitudeInput.setText(latitude);
        longitudeInput.setText(longitude);

        ListView cityListView = findViewById(R.id.city_list_view);
        cityListView.setAdapter(cityListAdapter = new CityListAdapter());

        Button helpBtn = findViewById(R.id.help_btn);
        helpBtn.setOnClickListener(click -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle(R.string.tm_help_btn)
                    .setMessage(R.string.tm_help_content)
                    .setPositiveButton(R.string.tm_yes, (c, arg) -> {

                    })
                    .create().show();
        });

        gotoFavoriteList = new Intent(TicketMasterActivity.this, FavoriteCitiesActivity.class);
        Button gotoFavoriteBtn = findViewById(R.id.favorite_btn);
        gotoFavoriteBtn.setOnClickListener(click -> {
            startActivity(gotoFavoriteList);
        });

        gotoCityDetail = new Intent(TicketMasterActivity.this, CityDetailActivity.class);


        progressBar = findViewById(R.id.progress_horizontal);
        Button searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(click -> {
            String lat = latitudeInput.getText().toString();
            String lng = longitudeInput.getText().toString();
            saveArguments(lat, lng);
            loadCityList(lat, lng);
        });



        cityListView.setOnItemClickListener((parent, view, position, id) -> {
            City city = cityList.get(position);
            gotoCityDetail.putExtra("country", city.getCountry());
            gotoCityDetail.putExtra("region", city.getRegion());
            gotoCityDetail.putExtra("city", city.getCity());
            gotoCityDetail.putExtra("currency", city.getCurrencyName());
            gotoCityDetail.putExtra("latitude", city.getLatitude());
            gotoCityDetail.putExtra("longitude", city.getLongitude());
            startActivity(gotoCityDetail);
        });
    }
    private void saveArguments(String latitude, String longitude) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("latitude", latitude);
        edit.putString("longitude", longitude);
        edit.commit();
    }
    /**
     * @param lat
     * @param lng
     */
    private void loadCityList(String lat, String lng) {
        cityList.clear();
        progressBar.setVisibility(View.VISIBLE);
        CitiesQuery citiesQuery = new CitiesQuery();
        citiesQuery.execute("https://api.geodatasource.com/cities?key=QVL5LMNQ9JF38MVEIQOIWYUDHBEQR5LE&lat=" + lat + "&lng=" + lng + "&format=JSON");
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    private class CityListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public Object getItem(int position) {
            return cityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return cityList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            City city = (City) getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            //make a new rowrow_city_layout
            View rowView = inflater.inflate(R.layout.activity_tm_row_data_layout, parent, false);

            TextView nameView = rowView.findViewById(R.id.city_name_text_view);
            nameView.setText(city.getCity());
            return rowView;
        }
    }
    private class CitiesQuery extends AsyncTask<String, Integer, String> {

        private List<City> cityResults = new ArrayList();

        /**
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

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONArray cityJSONArray = new JSONArray(result);
                JSONObject cityJSONObject = new JSONObject();


                for (int i = 0; i < cityJSONArray.length(); i++) {
                    cityJSONObject = cityJSONArray.getJSONObject(i);
                    City city = new City();
                    city.setCity(cityJSONObject.getString("city"));
                    city.setCountry(cityJSONObject.getString("country"));
                    city.setRegion(cityJSONObject.getString("region"));
                    city.setCurrencyName(cityJSONObject.getString("currency_name"));
                    city.setLatitude(cityJSONObject.getString("latitude"));
                    city.setLongitude(cityJSONObject.getString("longitude"));
                    cityResults.add(city);
                    publishProgress(i * (100 / cityJSONArray.length()));
                    progressBar.setProgress(i * (100 / cityJSONArray.length()));
                }
                publishProgress(100);
                progressBar.setProgress(100);

            } catch (Exception e) {
                String msg = e.getMessage();
                Log.e("Search cities", e.getMessage());

            }
            return "Search Done";
        }

        public void onPostExecute(String fromDoInBackground) {
            progressBar.setVisibility(View.INVISIBLE);
            cityList.addAll(this.cityResults);
            if (cityList.size() == 0) {
                Toast.makeText(TicketMasterActivity.this, "No city nearby", Toast.LENGTH_LONG).show();
            }
            cityListAdapter.notifyDataSetChanged();
        }
    }
}