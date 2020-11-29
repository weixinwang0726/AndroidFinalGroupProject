package com.example.androidfinalgroupproject.audio.fragment;

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
import com.example.androidfinalgroupproject.audio.AudioMainActivity;
import com.example.androidfinalgroupproject.audio.common.Album;
import com.example.androidfinalgroupproject.audio.common.AlbumDataSource;
import com.example.androidfinalgroupproject.audio.common.SongListAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
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
 * @Author: Yanan Cheng
 * @Date: 2020/11/19 5:22
 * @Version: 1.0
 */
public class AlbumDetailPage extends Fragment {

    private TextView mTextViewAlbum, mTextViewArtist;
    private ListView mListView;
    private SongListAdapter mListAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mImageButton;
    private AlbumDataSource mDataSource;

    private String albumName, artistName, albumId;
    private ArrayList<String> songs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_detail_fragment, null);

        ((AudioMainActivity) getActivity()).getToolbar().setTitle(getString(R.string.title_album_page));
        mTextViewAlbum = (TextView) view.findViewById(R.id.album);
        mTextViewArtist = (TextView) view.findViewById(R.id.artist);
        mListView = (ListView) view.findViewById(R.id.songs_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_album);
        mImageButton = (ImageButton) view.findViewById(R.id.like_button);
        mDataSource = ((AudioMainActivity) getActivity()).getDataSource();

        albumName = getArguments().getString("album");
        artistName = getArguments().getString("artist");
        albumId = getArguments().getString("albumId");

        mTextViewAlbum.setText("Album: " + albumName);
        mTextViewArtist.setText("Artist: " + artistName);

        // 获得新的url，加载album的歌曲数据
        String url = "https://theaudiodb.com/api/v1/json/1/track.php?m=" + albumId;
        SearchAlbum searchAlbum = new SearchAlbum();
        searchAlbum.execute(url);

        // 点击歌曲跳转到浏览器用google搜索
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://www.google.com/search?q=" + artistName + "+" + songs.get(position);
                Uri uri = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        // 将album添加到favorite列表中
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album album = new Album(albumId, artistName, albumName);
                if (mDataSource.isAlbumNotExists(album)) {
                    mDataSource.addToFavoriteList(album);
                    Snackbar.make(v, albumName + getString(R.string.snackbar_message), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction(getString(R.string.cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDataSource.deleteSelectedAlbum(album);
                                    Snackbar.make(v, getString(R.string.cancelled), BaseTransientBottomBar.LENGTH_LONG).show();
                                }
                            }).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.toast_message), Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    // 后台处理歌曲信息显示到listview里
    private class SearchAlbum extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);

                String line = reader.readLine();
                JSONObject jsonObject = new JSONObject(line);
                JSONArray jsonArray = jsonObject.getJSONArray("track");

                int process = 0;
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject songObject = jsonArray.getJSONObject(j);
                    songs.add(songObject.getString("strTrack"));

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
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mListAdapter = new SongListAdapter(getContext(), R.layout.album_list_row, songs);
            mListView.setAdapter(mListAdapter);
        }
    }
}
