package com.example.androidfinalgroupproject.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidfinalgroupproject.MainActivity;
import com.example.androidfinalgroupproject.R;
import com.example.androidfinalgroupproject.audio.AudioMainActivity;
import com.example.androidfinalgroupproject.audio.common.AlbumDataSource;
import com.example.androidfinalgroupproject.audio.fragment.FavoriteListPage;
import com.example.androidfinalgroupproject.audio.fragment.SearchMainPage;
import com.google.android.material.navigation.NavigationView;
/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/03
 * @Version: 1.0
 */
public class RecipeActivity extends AppCompatActivity   {


    SharedPreferences prefs;
    EditText RecipeKeyword;
    private DrawerLayout rDrawerLayout;
    private ActionBarDrawerToggle rDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    //object to use recipe search function
    private RecipeUserActivity user;
    private RecipeFunction input;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);  //load recipe search main activity page

       RecipeSearch page = new RecipeSearch();

        rDrawerLayout = (DrawerLayout) findViewById(R.id.r_drawer);  //load the drawer from layout
        toolbar = (Toolbar) findViewById(R.id.r_toolbar);  //toolbar
        navigationView = (NavigationView) findViewById(R.id.r_nav_view);  //navigation view for toolbar

        //initialize widgets and other layout
        setSupportActionBar(toolbar);
        rDrawerToggle = new ActionBarDrawerToggle(this, rDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        rDrawerLayout.addDrawerListener(rDrawerToggle);
        rDrawerToggle.setDrawerIndicatorEnabled(true);
        rDrawerToggle.syncState();


        //getting ready for user input data
        input = new RecipeFunction(this);
        input.open();


        // Using fragment to access database


        // listen to navigation item selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    // click HELP on menu show up the instruction for how to use recipe search
                    case R.id.recipe_help:

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(navigationView.getContext());

                        alertDialogBuilder.setTitle("HELP")
                                .setMessage(R.string.r_help_content)
                                .setPositiveButton(R.string.r_OK, (c, arg) -> {
                                })
                                .create().show();

                        break;

                    //click Search to navigate to search function

                    case R.id.search:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.recip_headly, page)
                                .addToBackStack(null)
                                .commit();
                        break;

                    //click favourite button to go to Favourite Recipe page*/
                    case R.id.r_menu_fav_btn:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.recip_headly, new RecipeFavActivity())
                                .addToBackStack(null)
                                .commit();
                        break;

                    case R.id.main_page:
                        //go back to main page
                        startActivity(new Intent(RecipeActivity.this, MainActivity.class));
                        break;

                    case R.id.item4:

                        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(navigationView.getContext());

                        alertDialogBuilder2.setTitle("Info")
                                .setMessage(R.string.about_this_project)
                                .setPositiveButton(R.string.r_OK, (c, arg) -> {
                                })
                                .create().show();

                        break;


                }
                rDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });


    }

    public RecipeFunction getDataSource () {
        return input;
    }

    //get Toolbar
    public Toolbar getToolbar () {
        return toolbar;
    }


}





