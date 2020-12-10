package com.example.androidfinalgroupproject.covid19;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidfinalgroupproject.R;


/**
 * COVID-19 Case Data
 * Instance CovidDetailsFragment of the fragment.
 */
public class CovidDetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Bundle dataFromActivity;

    private String mParam1;
    private String mParam2;

    public CovidDetailsFragment() {

    }

    public static CovidDetailsFragment newInstance(String param1, String param2) {
        CovidDetailsFragment fragment = new CovidDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // layout of this fragment
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_covid_details, container, false);

        TextView countryCode = (TextView)result.findViewById(R.id.countrycode_text_view);
        countryCode.setText( dataFromActivity.getString( "CountryCode" ) );

        TextView country = (TextView)result.findViewById(R.id.country_text_view);
        country.setText( dataFromActivity.getString( "Country" ) );

        TextView province = (TextView)result.findViewById(R.id.province_text_view);
        province.setText( dataFromActivity.getString( "Province" ) );

        TextView date = (TextView)result.findViewById(R.id.date_text_view);
        date.setText( dataFromActivity.getString( "Date" ) );

        TextView cases = (TextView)result.findViewById(R.id.case_text_view);
        cases.setText( dataFromActivity.getString( "Cases" ) );

        TextView lat = (TextView)result.findViewById(R.id.latitude_text_view);
        lat.setText( dataFromActivity.getString( "Lat" ) );

        TextView lon = (TextView)result.findViewById(R.id.longitude_text_view);
        lon.setText( dataFromActivity.getString( "Lon" ) );

        return result;
    }
}