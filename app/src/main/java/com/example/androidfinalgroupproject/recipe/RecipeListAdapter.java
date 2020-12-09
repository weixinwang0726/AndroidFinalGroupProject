package com.example.androidfinalgroupproject.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidfinalgroupproject.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/05
 * @Version: 1.0
 */

public class RecipeListAdapter extends ArrayAdapter<Recipe> {

    private List<Recipe> recipeList;
    int resource;

    public RecipeListAdapter(@NonNull Context context, int resource, @NonNull List<Recipe> rList) {
        super(context, resource, rList);
        this.recipeList = rList;
        this.resource = resource;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Recipe recipe = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.recipe_title);
        textView.setText(recipe.getTitle());
        return view;
    }


    @Override
    public Recipe getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }
}

