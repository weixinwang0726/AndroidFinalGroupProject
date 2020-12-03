package com.example.androidfinalgroupproject.covid19;

public class Province {

    private long id;
    private String country;
    private String countrycode;
    private String province;
    private String latitude;
    private String longitude;
    private String casenumber;
    private String date;


    public Province() {
    }


    public String getCountryCode() {
        return countrycode;
    }

    public void setCountryCode(String countrycode) {
        this.countrycode = countrycode;
    }

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

    public String getCase() { return casenumber; }

    public void setCase(String casenumber) { this.casenumber = casenumber; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
