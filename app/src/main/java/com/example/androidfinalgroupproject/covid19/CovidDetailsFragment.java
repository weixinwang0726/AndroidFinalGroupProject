package com.example.androidfinalgroupproject.covid19;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidfinalgroupproject.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CovidDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CovidDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Bundle dataFromActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CovidDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CovidDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        // Inflate the layout for this fragment
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_covid_details, container, false);

        TextView country = (TextView)result.findViewById(R.id.country_text_view);
        country.setText( dataFromActivity.getString( "Country" ) );

        TextView countryCode = (TextView)result.findViewById(R.id.countrycode_text_view);
        countryCode.setText( dataFromActivity.getString( "CountryCode" ) );

        TextView province = (TextView)result.findViewById(R.id.province_text_view);
        province.setText( dataFromActivity.getString( "Province" ) );

        TextView cases = (TextView)result.findViewById(R.id.case_text_view);
        cases.setText( dataFromActivity.getString( "Cases" ) );

        TextView date = (TextView)result.findViewById(R.id.date_text_view);
        date.setText( dataFromActivity.getString( "Date" ) );

        TextView lat = (TextView)result.findViewById(R.id.latitude_text_view);
        lat.setText( dataFromActivity.getString( "Lat" ) );

        TextView lon = (TextView)result.findViewById(R.id.longitude_text_view);
        lon.setText( dataFromActivity.getString( "Lon" ) );

        // Inflate the layout for this fragment
        return result;
    }
}