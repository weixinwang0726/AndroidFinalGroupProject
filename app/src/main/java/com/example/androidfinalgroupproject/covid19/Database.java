package com.example.androidfinalgroupproject.covid19;

public class Database {
    private String country;
    private String date;
    private long id;

    //constructor
    public Database()
    {
    }

    public Database( String country, String date )
    {
        this.country = country;
        this.date = date;
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


}
