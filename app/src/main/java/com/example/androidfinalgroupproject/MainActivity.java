package com.example.androidfinalgroupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidfinalgroupproject.audio.AudioMainActivity;
import com.example.androidfinalgroupproject.covid19.Covid19Case;
import com.example.androidfinalgroupproject.recipe.RecipeActivity;
import com.example.androidfinalgroupproject.ticketmaster.TicketMasterActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load toolbar
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

        Button goToAudioSearchBtn = findViewById(R.id.btn_to_audio_search);
        Intent goToAudioIntent = new Intent(MainActivity.this, AudioMainActivity.class);
        goToAudioSearchBtn.setOnClickListener(click -> {
            Toast.makeText(MainActivity.this, getString(R.string.audio_toast_message), Toast.LENGTH_LONG).show();
            startActivity(goToAudioIntent);
        });

        Button goToMasterTicketBtn = findViewById(R.id.btn_to_master_ticket);
        Intent goToMasterTicketIntent = new Intent(MainActivity.this, TicketMasterActivity.class);
        goToMasterTicketBtn.setOnClickListener(click -> {
            Toast.makeText(MainActivity.this, getString(R.string.mt_toast_message), Toast.LENGTH_LONG).show();
            startActivity(goToMasterTicketIntent);
        });

        navigationView.setItemIconTintList(null); //this line avoids the icons to appear shaded gray. src: https://stackoverflow.com/questions/31394265/navigation-drawer-item-icon-not-showing-original-colour
        navigationView.setNavigationItemSelectedListener(this);

        Button goToCovid19Btn = findViewById(R.id.btn_to_covid19);
        Intent goToCovide19Intent = new Intent(MainActivity.this, Covid19Case.class);
        goToCovid19Btn.setOnClickListener(click -> {
            Toast.makeText(MainActivity.this, getString(R.string.covid19_toast_message), Toast.LENGTH_LONG).show();
            startActivity(goToCovide19Intent);
        });

        Button goToRecipeBtn = findViewById(R.id.btn_to_recipe);
        Intent goToRecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
        goToRecipeBtn.setOnClickListener(click -> {
            Toast.makeText(MainActivity.this, getString(R.string.recipe_toast_message), Toast.LENGTH_LONG).show();
            startActivity(goToRecipeIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return true;
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
                startActivity(new Intent(MainActivity.this, AudioMainActivity.class));
                break;
            case R.id.ticket_master:
                startActivity(new Intent(MainActivity.this, TicketMasterActivity.class));
                break;
            case R.id.covid19:
                startActivity(new Intent(MainActivity.this, Covid19Case.class));
                break;

            case R.id.recipe:
                startActivity(new Intent(MainActivity.this, RecipeActivity.class));
                break;
        }
    }

    /**
     * this is the left top bar.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            case R.id.audio_search:
                startActivity(new Intent(MainActivity.this, AudioMainActivity.class));
                break;
            case R.id.ticket_master:
                startActivity(new Intent(MainActivity.this, TicketMasterActivity.class));
                break;
            case R.id.covid19:
                startActivity(new Intent(MainActivity.this, Covid19Case.class));
                break;
            case R.id.recipe:
                startActivity(new Intent(MainActivity.this, RecipeActivity.class));
                break;
        }
        return true;
    }
}