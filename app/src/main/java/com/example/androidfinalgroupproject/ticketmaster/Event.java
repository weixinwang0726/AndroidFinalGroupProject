package com.example.androidfinalgroupproject.ticketmaster;

import androidx.annotation.NonNull;

/**
 * Event Entity.
 */
public class Event {

    private long id;
    private String eventId ="";
    private String name ="";
    private String startDate = "";
    private double priceMin = 0;
    private double priceMax = 0;
    private String url = "";
    private String imageUrl = "";

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id["+this.getId()+"]");
        sb.append("\n eventId["+this.getEventId()+"]");
        sb.append("\n name["+this.getName()+"]");
        sb.append("\n startDate["+this.getStartDate()+"]");
        sb.append("\n priceMin["+this.getPriceMin()+"]");
        sb.append("\n priceMax["+this.getPriceMax()+"]");
        sb.append("\n url["+this.getUrl()+"]");
        sb.append("\n imageUrl["+this.getImageUrl()+"]");

        return sb.toString();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(double priceMin) {
        this.priceMin = priceMin;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(double priceMax) {
        this.priceMax = priceMax;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
