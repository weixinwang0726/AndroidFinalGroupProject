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

import java.util.List;

/**
 * @Author:Jingshan Guan
 * @Date: 2020/12/05
 * @Version: 1.0
 */
//a class for display recipe details
public class RecipeDetailList extends ArrayAdapter<String> {

    private List<String> rlist;
    int resource;

    public RecipeDetailList(@NonNull Context context, int resource, @NonNull List<String> resultlist) {
        super(context, resource, resultlist);
        this.rlist = resultlist;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String recipe = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);

        //put text into recipe title

        TextView textView = (TextView) view.findViewById(R.id.recipe_title);
        textView.setText(recipe);

        return view;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return rlist.get(position);
    }

    @Override
    public int getCount() {
        return rlist.size();
    }
}

