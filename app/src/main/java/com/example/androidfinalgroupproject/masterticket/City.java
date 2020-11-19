package com.example.androidfinalgroupproject.masterticket;

public class City {

    /**
     * country	string	Two-character country code based on ISO 3166.
     * region	string	Region or state name.
     * city	string	City name.
     * latitude	double	Latitude of a location.
     * longitude	double	Longitude of a location.
     * currency_code	string	Currency code based on ISO 4217.
     * currency_name	string	Currency name.
     * currency_symbol	string	Currency symbol.
     * sunrise	string	Time of sunrise. (hh:mm format in local time, i.e, 07:47).
     * sunset	string	Time of sunset. (hh:mm format in local time, i.e 19:50).
     * time_zone	string	UTC time zone (with DST supported).
     * distance_km	double 	Distance between the input coordinate and city coordinate in kilometers (km).
     */
    private long id;
    private String country;
    private String region;
    private String city;
    private String latitude;
    private String longitude;
    private String currencyCode;
    private String currencyName;
    private String currencySymbol;
    private String sunrise;
    private String sunset;
    private String timeZone;
    private String distanceKm;

    private boolean isFollowed;

    public City() {
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(String distanceKm) {
        this.distanceKm = distanceKm;
    }
}