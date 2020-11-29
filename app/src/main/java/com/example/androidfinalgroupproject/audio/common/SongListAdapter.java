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
 * @Date: 2020/11/19 9:46
 * @Version: 1.0
 */
public class SongListAdapter extends ArrayAdapter<String> {

    private List<String> mSongList;
    int resource;

    public SongListAdapter(@NonNull Context context, int resource, @NonNull List<String> songList) {
        super(context, resource, songList);
        this.mSongList = songList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String song = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.album_name);
        textView.setText(song);
        return view;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mSongList.get(position);
    }

    @Override
    public int getCount() {
        return mSongList.size();
    }
}
