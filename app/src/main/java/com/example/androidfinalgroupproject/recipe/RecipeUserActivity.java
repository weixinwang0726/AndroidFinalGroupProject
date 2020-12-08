package com.example.androidfinalgroupproject.recipe;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidfinalgroupproject.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/06
 * @Version: 1.0
 */

//class for user to load searched recipe, add to favourite, and delete from favourite
public class RecipeUserActivity extends Fragment {

    private TextView textTitle, textIngredient,textURL;
    private ListView rListView;
    private RecipeListAdapter rListAdapter;
    private ProgressBar pbar;
    private ImageButton favbtn;
    private RecipeFunction recipeData;
    //parameters for searching recipe through API
    private String rTitle, rIngredient, rURL;

    //create an Arraylist of Recipe object
    private ArrayList<String> rlist = new ArrayList<Recipe>();
    private Snackbar SnackBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details, null);

        //get the recipe details
        ((RecipeActivity) getActivity()).getToolbar().setTitle(getString(R.string.r_details));
        textTitle = (TextView) view.findViewById(R.id.recipe_title);  //title
        textIngredient = (TextView) view.findViewById(R.id.recipe_ingredient);  //ingredient
        textURL = (TextView) view.findViewById(R.id.recipe_url); //url


        //recipe search results in a listView
        rListView = (ListView) view.findViewById(R.id.recipe_list);
        //progress bar
        pbar = (ProgressBar) view.findViewById(R.id.progress_album);

        //favourite button
        favbtn = (ImageButton) view.findViewById(R.id.fav_button);
        //get data
        recipeData = ((RecipeActivity) getActivity()).getDataSource();



        //TODO

        rTitle=getArguments().getString("title");   //the search term for name or ingredient entered
        rIngredient = getArguments().getString("ingredient"); //ingredients that the recipe must include.



        textTitle.setText("Recipe Title: " + rTitle);
        textIngredient.setText( "Ingredients: " + rIngredient);


        //obtain recipe URL,  search for the recipe using name or ingredient
        String url = "http://www.recipepuppy.com/api/?i=" + rIngredient+ "&q=" + rTitle ;

        SearchRecipe sr = new SearchRecipe();
        sr.execute(url);


        //when click on the recipe url, launch the browse to open the url.

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url  = "http://www.recipepuppy.com/api/?i=" + rIngredient+ "&q=" + rTitle ;
                Uri uri = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        // add the selected recipe to favourite
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe r = new Recipe(rTitle, rIngredient, url);
                if (recipeData.isRecipeNotExists(r)) { //if the recipe has not added to favourite
                    recipeData.addToFavoriteList(r);//add to favourite
                    Snackbar.make(v, rTitle + getString(R.string.r_fav_added), Snackbar.LENGTH_LONG)  //alert user, recipe has added to favourite
                                    //  XXXXX recipe added to Favourite

                            .setAction(getString(R.string.cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recipeData.deleteSelectedRecipe(r);
                                    Snackbar.make(v, getString(R.string.cancelled), SnackBar.LENGTH_LONG).show();
                                }
                            }).show();
                } else { //recipe already saved in favourite
                    Toast.makeText(getContext(), getString(R.string.toast_message), Toast.LENGTH_LONG).show();  //alert user recipe already exists.
                }
            }
        });
        return view;
    }

    // load recipe details in ListView
    private class SearchRecipe extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);

                String line = reader.readLine();
                JSONObject jsonObject = new JSONObject(line);
                JSONArray jsonArray = jsonObject.getJSONArray("");

                int process = 0;
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject recipeObject = jsonArray.getJSONObject(j);
                    rlist.add(recipeObject.getString("strTrack"));

                    process += 100 / jsonArray.length();
                    publishProgress(process);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            pbar.setVisibility(View.INVISIBLE);
          rListAdapter = new RecipeListAdapter(getContext(), R.layout.recipe_list_row, rlist);
            //TODO
            rListView.setAdapter(rListAdapter);
        }
    }
}

