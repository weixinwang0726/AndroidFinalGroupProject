package com.example.androidfinalgroupproject.recipe;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidfinalgroupproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/05
 * @Version: 1.0
 */

//RecipeSearch class for recipe search function, in recipe search page
public class RecipeSearch extends Fragment {

    private EditText edit_text;
    private Button button;
    private ProgressBar pb;
    private ListView list_view;
    private RecipeListAdapter r_ListAdapter;
    private SharedPreferences sp;

    private List<Recipe> recipeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_search_page, null);

        ((RecipeActivity) getActivity()).getToolbar().setTitle(getString(R.string.recipe_header));

        // initialize EditText, search button, progress bar, ListView

        edit_text = (EditText) view.findViewById(R.id.Recipe_search_edit_text);

        button = (Button) view.findViewById(R.id.recipe_search_button);

        pb = (ProgressBar) view.findViewById(R.id.progress_recipe);

        list_view = (ListView) view.findViewById(R.id.recipe_search_result_list);

        // SharedPreferences to save the last recipe that was searched and show it the next time the application is launched/
        sp = getContext().getSharedPreferences("shared_preference", Context.MODE_PRIVATE);

        edit_text.setText(sp.getString("ingredient", ""));

        // search button action
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserType();

                if (recipeList.size() > 0) {
                    recipeList.clear();
                    r_ListAdapter.notifyDataSetChanged();
                }
                //connect to recipe search API ,format i = YYYY is the ingredients
                String api = "http://www.recipepuppy.com/api/?i=" + edit_text.getText().toString().trim();

                SearchRecipe searchRecipe = new SearchRecipe();

                searchRecipe.execute(api);
            }
        });

        // click on the search result to load recipe details using Bundle
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = recipeList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("title", recipe.getTitle());
                bundle.putString("ingredient", recipe.getIngredient());
                bundle.putString("url", recipe.getUrl());


                RecipeUserActivity detailrecipe = new RecipeUserActivity();
                detailrecipe.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recip_headly, detailrecipe)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    //save user's last input with SharedPreferences
    private void saveUserType() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ingredient", edit_text.getText().toString().trim());
        editor.commit();
    }

    // search recipe extends AsyncTask
    private class SearchRecipe extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                //create network connection:
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);

                String line = reader.readLine();
                JSONObject jsonObject = new JSONObject(line);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                int progress = 0;
                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject recipeObject = jsonArray.getJSONObject(i);
                    Recipe recipe = new Recipe();
                    recipe.setTitle(recipeObject.getString("title"));
                    recipe.setIngredient(recipeObject.getString("ingredients"));
                    recipe.setUrl(recipeObject.getString("href"));

                    recipeList.add(recipe);
                    progress += 100 / jsonArray.length();
                    publishProgress(progress);
                    Thread.sleep(50);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Log.e("TAG", "doInBackground: ", e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //show progress bar
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(values[0]);
        }

        //load data from database
        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.VISIBLE);

            r_ListAdapter = new RecipeListAdapter(getContext(), R.layout.recipe_list_row, recipeList);
            Toast.makeText(getContext(), getString(R.string.r_recipe_loading), Toast.LENGTH_LONG).show();
            list_view.setAdapter(r_ListAdapter);

            //Toast.makeText(getContext(), getString(R.string.r_recipe_loaded), Toast.LENGTH_LONG).show();
        }
    }
}
