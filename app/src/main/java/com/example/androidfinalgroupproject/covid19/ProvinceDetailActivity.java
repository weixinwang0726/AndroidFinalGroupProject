package com.example.androidfinalgroupproject.covid19;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalgroupproject.R;

public class ProvinceDetailActivity extends AppCompatActivity {

    String country;
    String countryCode;
    String latitude;
    String longitude;
    String provinceName;
    String caseNumber;
    long id;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_detail);

        Intent fromIntent = getIntent();
        country = fromIntent.getStringExtra("Country");
        provinceName = fromIntent.getStringExtra("Province");
        latitude = fromIntent.getStringExtra("Lat");
        longitude = fromIntent.getStringExtra("Lon");
        countryCode = fromIntent.getStringExtra("CountryCode");
        id = fromIntent.getLongExtra("id", -1);
        caseNumber = fromIntent.getStringExtra("Cases");
        date = fromIntent.getStringExtra("Date");


        TextView countryTextView = findViewById(R.id.country_text_view);
        TextView countryCodeTextView = findViewById(R.id.countrycode_text_view);
        TextView provinceTextView = findViewById(R.id.province_text_view);
        TextView caseTextView = findViewById(R.id.case_text_view);
        TextView dateTextView = findViewById(R.id.date_text_view);
        TextView latitudeTextView = findViewById(R.id.latitude_text_view);
        TextView longitudeTextView = findViewById(R.id.longitude_text_view);

        countryTextView.setText(country);
        countryCodeTextView.setText(countryCode);
        dateTextView.setText(date);
        latitudeTextView.setText(latitude);
        longitudeTextView.setText(longitude);
        provinceTextView.setText(provinceName);
        caseTextView.setText(caseNumber);

    }
}