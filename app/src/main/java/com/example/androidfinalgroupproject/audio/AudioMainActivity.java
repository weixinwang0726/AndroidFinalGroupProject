package com.example.androidfinalgroupproject.audio;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidfinalgroupproject.MainActivity;
import com.example.androidfinalgroupproject.R;
import com.example.androidfinalgroupproject.audio.common.AlbumDataSource;
import com.example.androidfinalgroupproject.audio.fragment.FavoriteListPage;
import com.example.androidfinalgroupproject.audio.fragment.SearchMainPage;
import com.example.androidfinalgroupproject.covid19.Covid19Case;
import com.example.androidfinalgroupproject.recipe.RecipeActivity;
import com.example.androidfinalgroupproject.ticketmaster.TicketMasterActivity;
import com.google.android.material.navigation.NavigationView;

/**
 * Course Name: CST2335_20F_22 Graphical Interface Programming (Android)
 * Class name:AudioMainActivity.java
 * Final Project The Audio Database api Topic Launching Activity
 * @author Yanan Cheng
 * @version 1.0, 11/19/2020
 *  Use only one activity and many fragments
 */


public class AudioMainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private SearchMainPage page;
    private boolean isTablet;
    private AlbumDataSource mDataSource;

    /**
     * User enters artist name, then AsyncTask use the information
     * open a new Activity and display detail data in it.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_main);
        page = new SearchMainPage();

        //Initial widget
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mActionBarDrawerToggle.syncState();
        isTablet = findViewById(R.id.search_page) != null;

        // initial database
        mDataSource = new AlbumDataSource(this);
        mDataSource.open();

        // Set NavigationDrawer
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.back_to_main:
                        startActivity(new Intent(AudioMainActivity.this, MainActivity.class));
                        break;
                    case R.id.nav_search:
                        if (isTablet) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.search_page, page)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, page)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        break;
                    case R.id.nav_favorite:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, new FavoriteListPage())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_help:
                        displayHelp();
                        break;
                }

                return true;
            }
        });

        if (isTablet) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.search_page, page)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, page)
                    .addToBackStack(null)
                    .commit();
        }
    }

    // set toolbar buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.audio_menu, menu);
        return true;
    }

    // set toolbar activities

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ticket_master:
                startActivity(new Intent(AudioMainActivity.this, TicketMasterActivity.class));
                break;
            case R.id.covid19:
                startActivity(new Intent(AudioMainActivity.this, Covid19Case.class));
                break;
            case R.id.recipe:
                startActivity(new Intent(AudioMainActivity.this, RecipeActivity.class));
                break;
            case R.id.audio_help:
                displayHelp();
                break;
        }
        return true;
    }

    private void displayHelp() {
        String title = "Audio Search ";
        String line1 = "This app will help you search singer";
        String line2 = "And you could save your favorite album";
        String line3 = "Created by Yanan Cheng";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioMainActivity.this);
        alertDialogBuilder.setTitle(title)
                .setMessage(line1 + " \n" + line2 + " \n" + line3)
                .setNegativeButton("Close", (click, arg) -> {
                    //You can add extra layout elements:
                    //.setView(getLayoutInflater().inflate(R.layout.row_layout, null) )

                }).create().show();
    }

    // Use other fragment ask for data source from album data source
    public AlbumDataSource getDataSource() {
        return mDataSource;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setPage(SearchMainPage page) {
        this.page = page;
    }
}