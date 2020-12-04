package com.example.androidfinalgroupproject.covid19;

public class Database {
    private String country;
    private String date;
    private long id;
    private String province;
    private String covidcase;

    public Database() {
    }

    public Database( String country, String date, String province, String covidcase ) {
        this.country = country;
        this.date = date;
        this.province = province;
        this.covidcase = covidcase;
    }

    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }

    public String getCovidcase(){
        return this.covidcase;
    }
    public void setCovidcase(String covidcase) {
        this.covidcase = covidcase;
    }

}
