package com.example.androidfinalgroupproject.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidfinalgroupproject.R;
import com.google.android.material.navigation.NavigationView;

public class RecipeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences prefs;
    EditText RecipeKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference for SharedPreferences
        String key = getString(R.string.r_search_keyword);
        prefs = getSharedPreferences(key, Context.MODE_PRIVATE);

        // get reserved email from SharedPreferences
        key = getString(R.string.r_search_keyword);
        String keyword= prefs.getString(key, ""); ;

        // push reserved email to edit control
        searfindViewById(R.id.editTextEmail);
        editEmail.setText(email);

        Button btnLogin = findViewById(R.id.login);

        btnLogin.setOnClickListener( v -> {
            Intent profilePage = new Intent(MainActivity.this, ProfileActivity.class);


            // get email user typed
            String emailTyped = editEmail.getText().toString();
            profilePage.putExtra(getString(R.string.youremail), emailTyped);

            startActivity(profilePage);
        });


        Button weatherButton= findViewById(R.id.weatherbutton);
        weatherButton.setOnClickListener(v -> {
            Intent weatherPage = new Intent(MainActivity.this,WeatherForecast.class);

            startActivity(weatherPage);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor  = prefs.edit();
        String key = getString(R.string.youremail);
        String emailtyped = editEmail.getText().toString();
        editor.putString(key, emailtyped);
        editor.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}





