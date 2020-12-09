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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/06
 * @Version: 1.0
 */

//class for user to load searched recipe, add to favourite, and delete from favourite
public class RecipeUserActivity extends Fragment {
   //widgets
    private TextView textTitle, textIngredient, textURL;
    private ListView rListView;
    private RecipeListAdapter rListAdapter;
    private ProgressBar pbar;
    private ImageButton favbtn;


    private RecipeFunction recipeData;
    //parameters for searching recipe through API
    private String rTitle, rIngredient, rURL;

    //create an Arraylist of Recipe object
    private ArrayList<Recipe> rlist = new ArrayList<Recipe>();
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
        pbar = (ProgressBar) view.findViewById(R.id.progress_recipe);

        //favourite button
        favbtn = (ImageButton) view.findViewById(R.id.fav_button);
        //get data
        recipeData = ((RecipeActivity) getActivity()).getDataSource();



        rTitle = getArguments().getString("title");   //the search term for name or ingredient entered
        rIngredient = getArguments().getString("ingredient"); //ingredients that the recipe must include.


        textTitle.setText("Recipe Title: " + rTitle);
        textIngredient.setText("Ingredients: " + rIngredient);

        //obtain recipe URL,  search for the recipe using name or ingredient
        // i.e   http://www.recipepuppy.com/api/?i=egg&q=omelet&p=3%22   for "egg" "omelet"
        String url = "http://www.recipepuppy.com/api/?i=" + rIngredient + "&q=" + rTitle + "&p=3";
        SearchRecipe sr = new SearchRecipe();
        sr.execute(url);

        //when click on the recipe url, launch the browse to open the url.

        //
        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "http://www.recipepuppy.com/api/?i=" + rIngredient + "&q=" + rTitle;
                Uri uri = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        // add the selected recipe to favourite
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe r = new Recipe(rTitle, rIngredient, url);
                if (recipeData.isRecipeNotExists(r)) { //whether the recipe has not added to favourite
                    recipeData.addToFavoriteList(r);//add to favourite
                    Snackbar.make(v, rTitle + getString(R.string.r_fav_added), Snackbar.LENGTH_LONG)
                            //alert user, recipe has added to favourite


                            .setAction(getString(R.string.cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recipeData.deleteSelectedRecipe(r);
                                    Snackbar.make(v, getString(R.string.cancelled), SnackBar.LENGTH_LONG).show();
                                }
                            }).show();
                } else { //recipe already saved in favourite
                    Toast.makeText(getContext(), getString(R.string.r_already_saved), Toast.LENGTH_LONG).show();  //alert user recipe already exists.
                }
            }
        });
        return view;
    }

    // load recipe details in ListView using AsyncTask
    private class SearchRecipe extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String title = "";
            String ingredient = "";
            String link = "";


            try {
                URL url = null;
                try {
                    url = new URL(strings[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream response = null;
                try {
                    response = connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //use JSON
/*
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
            */
                //end of JSon


                //use XMLPullParse
                XmlPullParserFactory factory = null;
                try {
                    factory = XmlPullParserFactory.newInstance();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                factory.setNamespaceAware(false);
                XmlPullParser xpp = null;

                try {
                    xpp = factory.newPullParser();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                try {
                    xpp.setInput(response, "UTF-8");
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                //now loop the XML

                int eventType = 0;
                try {
                    eventType = xpp.getEventType();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                try {
                    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equals("title")) {

                                try {
                                    title = xpp.nextText();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                }
                                publishProgress(25);
                            } else if (xpp.getName().equals("href")) {
                                try {
                                    link = xpp.nextText();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                }
                                publishProgress(50);

                            } else if (xpp.getName().equals("ingredients")) {
                                try {
                                    ingredient = xpp.nextText();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                }
                                publishProgress(75);
                            }

                            if (!title.equals("") && !link.equals("") && !ingredient.equals("")) {

                                rlist.add(new Recipe(title, ingredient, link));

                                title = "";
                                ingredient = "";
                                link = "";
                            }
                        }
                        try {
                            xpp.next();  //move to the next XML event
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }

                    }


                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }return "search finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(values[0]);
        }

        @Override
        public void onPostExecute(String s) {
            pbar.setVisibility(View.INVISIBLE);
            rListAdapter = new RecipeListAdapter(getContext(), R.layout.recipe_list_row, rlist);

            rListView.setAdapter(rListAdapter);
        }

    }
}
