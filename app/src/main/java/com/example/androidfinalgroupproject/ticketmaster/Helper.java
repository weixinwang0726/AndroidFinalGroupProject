package com.example.androidfinalgroupproject.ticketmaster;

import android.content.Intent;
import android.content.SharedPreferences;

import com.example.androidfinalgroupproject.common.Const;


/**
 * assitant class
 */
public class Helper {

    /**
     * save the params into the shared preferences.
     * @param prefs
     * @param city
     * @param radius
     */
    public void saveArguments(SharedPreferences prefs,String city, String radius) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(Const.PREF_TM_CITY, city);
        edit.putString(Const.PREF_TM_RADIUS, radius);
        edit.commit();
    }

    /**
     * save the event's params to pass to the next page.
     * @param intent
     * @param event
     */
    public void saveEventToExtra(Intent intent, Event event) {
        intent.putExtra(Const.PARAMS_TM_EVENT_ID, event.getEventId());
        intent.putExtra(Const.PARAMS_TM_NAME, event.getName());
        intent.putExtra(Const.PARAMS_TM_START_DATE, event.getStartDate());
        intent.putExtra(Const.PARAMS_TM_PRICE_MIN, event.getPriceMin());
        intent.putExtra(Const.PARAMS_TM_PRICE_MAX, event.getPriceMax());
        intent.putExtra(Const.PARAMS_TM_URL, event.getUrl());
        intent.putExtra(Const.PARAMS_TM_IMAGE_URL, event.getImageUrl());
    }

    /**
     * use the params to create a new event.
     * @param e
     * @param fromIntent
     * @return
     */
    public Event initiateEventWithExtra(Event e,Intent fromIntent) {
        e.setName(fromIntent.getStringExtra(Const.PARAMS_TM_NAME));
        e.setEventId(fromIntent.getStringExtra(Const.PARAMS_TM_EVENT_ID));
        e.setStartDate(fromIntent.getStringExtra(Const.PARAMS_TM_START_DATE));
        e.setPriceMin(fromIntent.getDoubleExtra(Const.PARAMS_TM_PRICE_MIN,0.0));
        e.setPriceMax(fromIntent.getDoubleExtra(Const.PARAMS_TM_PRICE_MAX,0.0));
        e.setUrl(fromIntent.getStringExtra(Const.PARAMS_TM_URL));
        e.setImageUrl(fromIntent.getStringExtra(Const.PARAMS_TM_IMAGE_URL));
        return e;
    }

}
