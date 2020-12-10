package com.example.androidfinalgroupproject.covid19;

public class Database {


    private long id;
    private String country;
    private String countrycode;
    private String province;
    private String latitude;
    private String longitude;
    private String casenumber;
    private String date;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCasenumber() {
        return casenumber;
    }

    public void setCasenumber(String casenumber) {
        this.casenumber = casenumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Database() {
    }

    public Database( String country, String date )
    {
        this.country = country;
        this.date = date;
    }

    public Database( String country, String date, String province, String covidcase ) {
        this.country = country;
        this.date = date;
        this.province = province;
        this.casenumber = covidcase;
    }


}
