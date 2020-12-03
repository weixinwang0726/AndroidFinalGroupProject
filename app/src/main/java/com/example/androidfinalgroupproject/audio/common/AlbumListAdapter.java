package com.example.androidfinalgroupproject.audio.common;

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
 * @Author: Yanan Cheng
 * @Date: 2020/11/19 7:45
 * @Version: 1.0
 */
public class AlbumListAdapter extends ArrayAdapter<Album> {

    private List<Album> mAlbumList;
    int resource;

    public AlbumListAdapter(@NonNull Context context, int resource, @NonNull List<Album> albumList) {
        super(context, resource, albumList);
        this.mAlbumList = albumList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Album album = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.album_name);
        textView.setText(album.getStrAlbum());
        return view;
    }

    @Nullable
    @Override
    public Album getItem(int position) {
        return mAlbumList.get(position);
    }

    @Override
    public int getCount() {
        return mAlbumList.size();
    }
}
