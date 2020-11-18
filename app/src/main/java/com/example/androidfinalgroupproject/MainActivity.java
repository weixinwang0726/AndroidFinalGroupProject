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

import com.example.androidfinalgroupproject.covid19.Covid19Activity;
import com.example.androidfinalgroupproject.masterticket.MasterTicketActivity;
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

        Button goToMasterTicketBtn = findViewById(R.id.btn_to_master_ticket);
        Intent goToMasterTicketIntent = new Intent(MainActivity.this, MasterTicketActivity.class);
        goToMasterTicketBtn.setOnClickListener(click -> {
            Toast.makeText(MainActivity.this, getString(R.string.mt_toast_message), Toast.LENGTH_LONG).show();
            startActivity(goToMasterTicketIntent);
        });

        navigationView.setItemIconTintList(null); //this line avoids the icons to appear shaded gray. src: https://stackoverflow.com/questions/31394265/navigation-drawer-item-icon-not-showing-original-colour
        navigationView.setNavigationItemSelectedListener(this);

        Button goToCovid19Btn = findViewById(R.id.btn_to_covid19);
        Intent goToCovide19Intent = new Intent(MainActivity.this, Covid19Activity.class);
        goToCovid19Btn.setOnClickListener(click -> {
            Toast.makeText(MainActivity.this, getString(R.string.covid19_toast_message), Toast.LENGTH_LONG).show();
            startActivity(goToCovide19Intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switchBetweenActivity(item.getItemId());
        return false;
    }
    public void switchBetweenActivity(int id) {
        switch (id){
            case R.id.ticket_master:
                startActivity(new Intent(MainActivity.this, MasterTicketActivity.class));
                break;
            case R.id.covid19:
                startActivity(new Intent(MainActivity.this, Covid19Activity.class));
                break;


        }
    }
}