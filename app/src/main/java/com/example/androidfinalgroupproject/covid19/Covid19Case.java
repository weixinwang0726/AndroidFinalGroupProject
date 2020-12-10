package com.example.androidfinalgroupproject.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalgroupproject.MainActivity;
import com.example.androidfinalgroupproject.R;
import com.example.androidfinalgroupproject.audio.AudioMainActivity;
import com.example.androidfinalgroupproject.recipe.RecipeActivity;
import com.example.androidfinalgroupproject.ticketmaster.TicketMasterActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class Covid19Case extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private SharedPreferences prefs;

    private List<Province> provinceList = new ArrayList<>();
    private List<Database> databaseList = new ArrayList<>();

    ListView provinceListView = null;
    ListView databaseListView = null;

    public static final String ITEM_SELECTED = "ITEM";

    private ProvinceListAdapter provinceListAdapter;
    private DatabaseListAdapter databaseListAdapter;
    SQLiteDatabase db;
    ProgressBar progressBar;

    Intent gotoProvinceDetail;

    CovidDetailsFragment dFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_case);

        boolean isTablet = findViewById(R.id.province_list_view) != null;

        /*
         set toolbar:
         */
        Toolbar tBar = (Toolbar)findViewById(R.id.covidToolBar);
        setSupportActionBar(tBar);
        /*
         navigation view:
         */
        DrawerLayout drawer = findViewById(R.id.covid_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.covidopen, R.string.covidclose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);//this line avoids the icons to appear shaded gray
        navigationView.setNavigationItemSelectedListener(this);

        /*
        save into query
         */
        prefs = getSharedPreferences("searchArguments", Context.MODE_PRIVATE);
        String country = prefs.getString("country", "");
        String date = prefs.getString("date", "");

        EditText countryInput = findViewById(R.id.country_input);
        EditText dateInput = findViewById(R.id.date_input);

        countryInput.setText(country);
        dateInput.setText(date);

        provinceListView = findViewById(R.id.province_list_view);
        provinceListView.setAdapter(provinceListAdapter = new ProvinceListAdapter());

        databaseListView = findViewById(R.id.database_list_view);
        databaseListView.setAdapter(databaseListAdapter = new DatabaseListAdapter());


        /*
        province details
         */
        gotoProvinceDetail = new Intent(Covid19Case.this, ProvinceDetailActivity.class);

        /*
        progress bar
         */
        progressBar = findViewById(R.id.progress_horizontal);
        /*
        search button
         */
        @SuppressLint("UseSwitchCompatOrMaterialCode")

        Button searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(click -> {
            String countryIn = countryInput.getText().toString();
            String dateIn = dateInput.getText().toString();
            saveArguments(countryIn, dateIn);
            loadProvinceList(countryIn, dateIn);

        });

        /*
         save button
         */
        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(click -> {
            String message = null;
            saveData(provinceList);
            message = "Save search results";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            loadDataFromDatabase();
        });

        /*
            display province details
         */
        provinceListView.setOnItemClickListener((parent, view, position, id) -> {
            Province province = provinceList.get(position);
            dFragment = new CovidDetailsFragment();
            Bundle d = new Bundle();
            d.putString("Country", province.getCountry());
            d.putString("CountryCode", province.getCountryCode());
            d.putString("Province", province.getProvince());
            d.putString("Cases", province.getCase());
            d.putString("Date", province.getDate());
            d.putString("Lat", province.getLatitude());
            d.putString("Lon", province.getLongitude());

            if(isTablet)
            {
                dFragment.setArguments(d);
                //load the fragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.covidfragmentLocation, dFragment)
                        .commit();
            }
            else
            {
                Intent nextActivity = new Intent(Covid19Case.this, ProvinceDetailActivity.class);
                nextActivity.putExtras(d);
                startActivity(nextActivity);
            }
        });

        databaseListView.setOnItemClickListener((parent, view, position, id) -> {
            getAllDataFromDatabase(databaseList.get(position));
        });

        //delete from database for long clicking item: use for loop to delete?
        databaseListView.setOnItemLongClickListener( (p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            Database selectedRec = databaseList.get(pos);
            alertDialogBuilder.setTitle(R.string.covid_delete_msg)
                    .setMessage( getString(R.string.delDBRec))

                    .setPositiveButton(R.string.covid_delBtn, (click, arg) -> {
                        deleteData(selectedRec);
                        Log.e("delete:",""+ selectedRec.getId() );
                        databaseList.remove(pos);
                        databaseListAdapter.notifyDataSetChanged();

                        Snackbar.make(saveBtn, getResources().getString(R.string.covid_switch_message),
                                Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.covid_undo),
                                new View.OnClickListener(){
                                    public void onClick(View v){
                                        addData(selectedRec);
                                        databaseList.add(selectedRec);
                                        databaseListAdapter.notifyDataSetChanged();
                                    }
                                }).show();

                    })
                    //action of No button
                    .setNegativeButton(R.string.covid_undoBtn, (click, arg) -> { })
                    .create().show();
            return false; //change from true to false
        });
    }

    private void saveArguments(String country, String date) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("country", country);
        edit.putString("date", date);
        edit.commit();
    }

    /*
        parse and convert date
     */
    private void loadProvinceList(String country, String date) {
        provinceList.clear();
        progressBar.setVisibility(View.VISIBLE);

        ProvincesQuery provincesQuery = new ProvincesQuery();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try {
            d1 = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            d1 = Calendar.getInstance().getTime();

        }
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        c.add(Calendar.DAY_OF_MONTH,1);
        Date d2 = c.getTime();
        String datePlusOne = df.format(d2);

        String url = String.format("https://api.covid19api.com/country/%s/status/confirmed/live?from=%sT00:00:00Z&to=%sT23:59:59Z", country, date,date);
        //String url = String.format("https://api.covid19api.com/country/%s/status/confirmed/live?from=%sT00:00:00Z&to=%sT00:00:00Z", country, date, datePlusOne);
        provincesQuery.execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covid_menu, menu);
        return true;
    }

    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.item1:
                startActivity(new Intent(Covid19Case.this, TicketMasterActivity.class));
                message = "Go to TicketMaster Page";
                break;
            case R.id.item2:
                startActivity(new Intent(Covid19Case.this, RecipeActivity.class));
                message = "Go to Recipe Page";
                break;
            case R.id.main_page:
                startActivity(new Intent(Covid19Case.this, AudioMainActivity.class));
                message = "Go to Audio Page";
                break;
            case R.id.item4:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle(R.string.covid_help_text)
                        .setMessage(R.string.covid_help_content)
                        .setPositiveButton(R.string.covid_yes, (c, arg) -> {

                        })
                        .create().show();
                message = "Show help";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

    /*
     *the OnNavigationItemSelected interface
     */
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.item1:
                //Go to Main Page
                startActivity(new Intent(Covid19Case.this, MainActivity.class));
                message = "Show main page";
                break;
            case R.id.item2:
                //show saved records in Database: country and date
                loadDataFromDatabase();
                message = "Show Database";
                break;
            case R.id.main_page:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle(R.string.covid_help_text)
                        .setMessage(R.string.covid_help_content)
                        .setPositiveButton(R.string.covid_yes, (c, arg) -> {

                        })
                        .create().show();
                message = "Show help";
                break;
            case R.id.item4:
                message = "Covid-19 activity written by Ziyin Yan";
                break;

        }
        DrawerLayout drawerLayout = findViewById(R.id.covid_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
    /*
     * save search result to database
     */
    private void saveData( List<Province> resultList )
    {
        ProvinceOpener provinceOpener = new ProvinceOpener(this);

        db = provinceOpener.getWritableDatabase();
       provinceOpener.onDowngrade(db,1,1);
        for (Province result : resultList)
        {
            //add to the database
            ContentValues newRowValues = new ContentValues();
            //add all columns:
            newRowValues.put( ProvinceOpener.COL_COUNTRY, result.getCountry() );
            newRowValues.put( ProvinceOpener.COL_DATE, result.getDate() );
            newRowValues.put( ProvinceOpener.COL_PROVINCE, result.getProvince() );
            newRowValues.put( ProvinceOpener.COL_CASE_NUMBER, result.getCase() );


            newRowValues.put(ProvinceOpener.COL_COUNTRY_CODE, result.getCountryCode() );
            newRowValues.put(ProvinceOpener.COL_LONGGITUDE, result.getLongitude() );
            newRowValues.put(ProvinceOpener.COL_LATITUDE, result.getLatitude() );

            //insert into the database:
            long newId = db.insert( ProvinceOpener.TABLE_NAME, null, newRowValues );
        }
    }
    /*
     * delete data in database
     */
    private void deleteData(Database i){
        db.delete( ProvinceOpener.TABLE_NAME, ProvinceOpener.COL_COUNTRY + "= ? and " + ProvinceOpener.COL_DATE + "= ?",
                new String[] {i.getCountry(), i.getDate()} );
    }

    private void addData(Database i){
        ContentValues newRowValues = new ContentValues();
        //add COUNTRY columns:
        newRowValues.put( ProvinceOpener.COL_COUNTRY, i.getCountry() );
        newRowValues.put( ProvinceOpener.COL_DATE, i.getDate() );
        newRowValues.put( ProvinceOpener.COL_PROVINCE, i.getProvince() );
        newRowValues.put( ProvinceOpener.COL_CASE_NUMBER, i.getCasenumber());
        long insert = db.insert(ProvinceOpener.TABLE_NAME, null,newRowValues);
    }
    /*
     * load record from Database
     */
    private void getAllDataFromDatabase(Database database){
        provinceList.clear();
        ProvinceOpener dbOpener = new ProvinceOpener(this);
        db = dbOpener.getWritableDatabase();


        String [] columns = {ProvinceOpener.COL_COUNTRY,ProvinceOpener.COL_COUNTRY_CODE,
                ProvinceOpener.COL_LATITUDE,ProvinceOpener.COL_LONGGITUDE,
                ProvinceOpener.COL_DATE, ProvinceOpener.COL_PROVINCE, ProvinceOpener.COL_CASE_NUMBER
        };
        String[] args = {database.getCountry(), database.getDate()};
        Cursor results = db.query(false, ProvinceOpener.TABLE_NAME, columns, ProvinceOpener.COL_COUNTRY + "=?" + " and "  +
                ProvinceOpener.COL_DATE + "=?", args,null, null, null, null);

        while(results.moveToNext())
        {

            Province p = new Province();
            p.setCountry(results.getString(results.getColumnIndex(ProvinceOpener.COL_COUNTRY)));
            p.setCountryCode(results.getString(results.getColumnIndex(ProvinceOpener.COL_COUNTRY_CODE)));
            p.setLatitude(results.getString(results.getColumnIndex(ProvinceOpener.COL_LATITUDE)));
            p.setLongitude(results.getString(results.getColumnIndex(ProvinceOpener.COL_LONGGITUDE)));
            p.setDate(results.getString(results.getColumnIndex(ProvinceOpener.COL_DATE)));
            p.setProvince(results.getString(results.getColumnIndex(ProvinceOpener.COL_PROVINCE)));
            p.setCase(results.getString(results.getColumnIndex(ProvinceOpener.COL_CASE_NUMBER)));


            provinceList.add(p);
        }
        provinceListAdapter.notifyDataSetChanged();
    }


    /*
     * load Database
     */
    private void loadDataFromDatabase()
    {
        databaseList.clear();
        ProvinceOpener dbOpener = new ProvinceOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {ProvinceOpener.COL_COUNTRY, ProvinceOpener.COL_DATE};
        //list country and date from the database:
        Cursor results = db.query(true, ProvinceOpener.TABLE_NAME, new String[]{"COUNTRY", "DATE"}, "COUNTRY not null and DATE not null", null,null, null, null, null);

        int countryColumnIndex = results.getColumnIndex(ProvinceOpener.COL_COUNTRY);
        int dateColumnIndex = results.getColumnIndex(ProvinceOpener.COL_DATE);

        List<Database> dbResults = new ArrayList<Database>();
        while(results.moveToNext())
        {
            String country = results.getString(countryColumnIndex);
            String date = results.getString(dateColumnIndex);
            dbResults.add( new Database( country, date ));
        }

        databaseList.addAll( dbResults );
        if( databaseList.size() > 0 )
        {
            databaseListAdapter.notifyDataSetChanged();
        }
    }

    private class DatabaseListAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return databaseList.size();
        }

        @Override
        public Object getItem(int position) {
            return databaseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return databaseList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Database databaseRec = (Database) getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            View rowView = inflater.inflate(R.layout.row_countrydate_layout, parent, false);

            TextView countryView = rowView.findViewById( R.id.country_name_text_view);
            countryView.setText(databaseRec.getCountry());

            TextView dateView = rowView.findViewById( R.id.date_text_view );
            dateView.setText( databaseRec.getDate() );

            return rowView;
        }
    }

    private class ProvinceListAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return provinceList.size();
        }

        @Override
        public Object getItem(int position) {
            return provinceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return provinceList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Province province = ( Province ) getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            //make a new row
            View rowView = inflater.inflate(R.layout.row_province_layout, parent, false);

            TextView provinceView = rowView.findViewById( R.id.province_name_text_view );
            provinceView.setText(province.getProvince());

            TextView caseView = rowView.findViewById( R.id.province_case_text_view );
            caseView.setText( province.getCase() );

            return rowView;
        }
    }

    private class ProvincesQuery extends AsyncTask<String, Integer, String>
    {

        private List<Province> provinceResults = new ArrayList();


        @Override
        protected String doInBackground(String... args) {

            try {
                //create a URL object
                URL url = new URL(args[0]);

                //open connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //Build response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON
                JSONArray provinceJSONArray = new JSONArray(result);
                JSONObject provinceJSONObject = new JSONObject();


                for (int i = 0; i < provinceJSONArray.length(); i++) {
                    provinceJSONObject = provinceJSONArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvince(provinceJSONObject.getString("Province"));
                    province.setCountry(provinceJSONObject.getString("Country"));
                    province.setCase(provinceJSONObject.getString("Cases"));
                    province.setCountryCode(provinceJSONObject.getString("CountryCode"));
                    province.setDate(provinceJSONObject.getString("Date"));
                    province.setLatitude(provinceJSONObject.getString("Lat"));
                    province.setLongitude(provinceJSONObject.getString("Lon"));

                    provinceResults.add(province);
                    publishProgress(i * (100 / provinceJSONArray.length()));
                    progressBar.setProgress(i * (100 / provinceJSONArray.length()));
                }
                publishProgress(100);
                progressBar.setProgress(100);

            } catch (Exception e) {
                String msg = e.getMessage();
                Log.e("Search provinces", e.getMessage());

            }
            return "Search Done";
        }

        public void onPostExecute(String fromDoInBackground) {
            progressBar.setVisibility(View.INVISIBLE);
            provinceList.addAll(this.provinceResults);
            if (provinceList.size() == 0) {
                Toast.makeText(Covid19Case.this, "No case returned.", Toast.LENGTH_LONG).show();
            }
            provinceListAdapter.notifyDataSetChanged();
        }
    }


}