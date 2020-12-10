package com.example.androidfinalgroupproject.recipe;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.androidfinalgroupproject.R;

import java.util.List;

/**
 * @Author:Jingshan Guan
 * @Date: 2020/12/05
 * @Version: 1.0
 */

public class RecipeFavActivity extends Fragment {


    private ListView favListView;
    private RecipeListAdapter rAdapter;
    private RecipeFunction input;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fav, null);

        ((RecipeActivity) getActivity()).getToolbar().setTitle(getString(R.string.title_favorite));
        favListView = (ListView) view.findViewById(R.id.r_fav_list);
        input = ((RecipeActivity) getActivity()).getDataSource();

        //get all saved to favourite recipes into a list
        List<Recipe> fav_recipe = input.getAllFavoriteRecipe();
        rAdapter = new RecipeListAdapter(getContext(), R.layout.recipe_list_row, fav_recipe);
        favListView.setAdapter(rAdapter);

        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe r = fav_recipe.get(position);
                Bundle bundle = new Bundle();


                bundle.putString("title", r.getTitle());
                bundle.putString("ingredient", r.getIngredient());
                bundle.putString("url", r.getUrl());


                RecipeUserActivity page = new RecipeUserActivity();


                page.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recip_headly, page)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // long click to delete selected recipe
        favListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe r = fav_recipe.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(getString(R.string.r_delete_alert) + r.getTitle())
                        .setMessage(getString(R.string.r_fave_deleted))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.r_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                input.deleteSelectedRecipe(r);
                                //remove
                                fav_recipe.remove(position);
                                rAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        return view;
    }
}




