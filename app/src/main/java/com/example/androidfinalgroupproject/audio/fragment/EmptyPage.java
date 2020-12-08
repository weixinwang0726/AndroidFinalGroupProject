package com.example.androidfinalgroupproject.audio.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidfinalgroupproject.R;

/**
 * @Author: Yanan Cheng
 * @Date: 2020/12/6 21:34
 * @Version: 1.0
 */
public class EmptyPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_empty_page, null);

        Bundle bundle = new Bundle();
        bundle.putString("artist", getArguments().getString("artist"));
        bundle.putString("album", getArguments().getString("album"));
        bundle.putString("albumId", getArguments().getString("albumId"));

        AlbumDetailPage detailPage = new AlbumDetailPage();
        detailPage.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.empty_page, detailPage)
                .commit();
        return view;
    }
}
