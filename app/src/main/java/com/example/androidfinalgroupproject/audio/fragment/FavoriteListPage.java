package com.example.androidfinalgroupproject.audio.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidfinalgroupproject.R;
import com.example.androidfinalgroupproject.audio.AudioMainActivity;
import com.example.androidfinalgroupproject.audio.common.Album;
import com.example.androidfinalgroupproject.audio.common.AlbumDataSource;
import com.example.androidfinalgroupproject.audio.common.AlbumListAdapter;

import java.util.List;

/**
 * @Author: Yanan Cheng
 * @Date: 2020/11/19 13:44
 * @Version: 1.0
 */
public class FavoriteListPage extends Fragment {
    private ListView mListView;
    private AlbumListAdapter mAdapter;
    private AlbumDataSource mDataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_favorite_list_fragment, null);

        ((AudioMainActivity) getActivity()).getToolbar().setTitle(getString(R.string.title_favorite));
        mListView = (ListView) view.findViewById(R.id.favorite_list);
        mDataSource = ((AudioMainActivity) getActivity()).getDataSource();

        // 从数据库里取出所有albums
        List<Album> albums = mDataSource.getAllFavoriteAlbums();
        mAdapter = new AlbumListAdapter(getContext(), R.layout.album_list_row, albums);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = albums.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("artist", album.getArtist());
                bundle.putString("album", album.getStrAlbum());
                bundle.putString("albumId", album.getIdAlbum());
                AlbumDetailPage page = new AlbumDetailPage();
                page.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, page)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // 长按删除选中的album
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = albums.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.title_delete) + album.getStrAlbum())
                        .setMessage(getString(R.string.delete_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDataSource.deleteSelectedAlbum(album);
                                albums.remove(position);
                                mAdapter.notifyDataSetChanged();
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
