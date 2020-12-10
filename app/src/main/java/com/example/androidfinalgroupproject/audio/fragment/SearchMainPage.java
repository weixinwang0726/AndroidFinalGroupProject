package com.example.androidfinalgroupproject.audio.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

    /**
     * List all the albums
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_search_page, null);

        ((AudioMainActivity) getActivity()).getToolbar().setTitle(getString(R.string.audio_search));

        // initial widget
        mEditText = (EditText) view.findViewById(R.id.search_edit_text);
        mButton = (Button) view.findViewById(R.id.search_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_audio);
        mListView = (ListView) view.findViewById(R.id.album_list);

        // loading last time data
        mSharedPreferences = getContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
        mEditText.setText(mSharedPreferences.getString("Artist", ""));

        if (albumList.size() > 0) {
            mListView.setAdapter(mAdapter);
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserType();
                closeKeyboard();
                if (albumList.size() > 0) {
                    albumList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                String api = "https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s="
                        + mEditText.getText().toString().trim();
                SearchArtist searchArtist = new SearchArtist();
                // loading url database
                searchArtist.execute(api);
                ((AudioMainActivity) getActivity()).setPage(getSearchMainPage());
            }
        });

        // click album any row of list to the album detail fragment
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = albumList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("artist", album.getArtist());
                bundle.putString("album", album.getStrAlbum());
                bundle.putString("albumId", album.getIdAlbum());

                EmptyPage emptyPage = new EmptyPage();
                emptyPage.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, emptyPage)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    // hide keyboard
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * Record the information that the user last entered
     */
    private void saveUserType() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("Artist", mEditText.getText().toString().trim());
        editor.commit();
    }

    /**JSON data is processed in the background
     *
     */
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

        /**display
         *
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter = new AlbumListAdapter(getContext(), R.layout.album_list_row, albumList);
            mListView.setAdapter(mAdapter);
            Toast.makeText(getContext(), getString(R.string.load_albums), Toast.LENGTH_LONG).show();
        }
    }

    private SearchMainPage getSearchMainPage() {
        return this;
    }
}
