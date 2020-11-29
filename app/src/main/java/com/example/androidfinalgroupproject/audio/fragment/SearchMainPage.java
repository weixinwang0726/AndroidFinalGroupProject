package com.example.androidfinalgroupproject.audio.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.androidfinalgroupproject.audio.AudioMainActivity;
import com.example.androidfinalgroupproject.audio.common.Album;
import com.example.androidfinalgroupproject.audio.common.AlbumListAdapter;

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
 * @Author: Yanan Cheng
 * @Date: 2020/11/18 23:07
 * @Version: 1.0
 */
public class SearchMainPage extends Fragment {

    private EditText mEditText;
    private Button mButton;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private AlbumListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;

    private List<Album> albumList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_search_page, null);

        ((AudioMainActivity) getActivity()).getToolbar().setTitle(getString(R.string.audio_search));

        // 初始化widget
        mEditText = (EditText) view.findViewById(R.id.search_edit_text);
        mButton = (Button) view.findViewById(R.id.search_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_audio);
        mListView = (ListView) view.findViewById(R.id.album_list);

        // 加载上次输入记录
        mSharedPreferences = getContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
        mEditText.setText(mSharedPreferences.getString("Artist", ""));

        // 设置搜索按钮
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserType();
                if (albumList.size() > 0) {
                    albumList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                String api = "https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s="
                        + mEditText.getText().toString().trim();
                SearchArtist searchArtist = new SearchArtist();
                // 传入url开始加载数据
                searchArtist.execute(api);
            }
        });

        // 点击album列表任意一行跳转到对应的album detail界面
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = albumList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("artist", album.getArtist());
                bundle.putString("album", album.getStrAlbum());
                bundle.putString("albumId", album.getIdAlbum());

                AlbumDetailPage detailPage = new AlbumDetailPage();
                detailPage.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, detailPage)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    /**
     * 记录用户上次输入的信息
     */
    private void saveUserType() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("Artist", mEditText.getText().toString().trim());
        editor.commit();
    }

    // 后台处理JSON数据
    private class SearchArtist extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);

                String line = reader.readLine();
                JSONObject jsonObject = new JSONObject(line);
                JSONArray jsonArray = jsonObject.getJSONArray("album");

                int progress = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject albumObject = jsonArray.getJSONObject(i);
                    Album album = new Album();
                    album.setArtist(albumObject.getString("strArtist"));
                    album.setIdAlbum(albumObject.getString("idAlbum"));
                    album.setStrAlbum(albumObject.getString("strAlbum"));

                    albumList.add(album);
                    progress += 100 / jsonArray.length();
                    publishProgress(progress);
                    // 防止进度条走动太快，加上了手动延时
                    Thread.sleep(50);
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

        // 数据加载完毕，显示出来
        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter = new AlbumListAdapter(getContext(), R.layout.album_list_row, albumList);
            mListView.setAdapter(mAdapter);
            Toast.makeText(getContext(), getString(R.string.load_albums), Toast.LENGTH_LONG).show();
        }
    }
}
