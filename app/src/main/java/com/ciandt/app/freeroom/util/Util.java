package com.ciandt.app.freeroom.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.model.Building;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 26/08/2015.
 */
public class Util {

    public static void saveURL(Context context, String url) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.key_url_service), url);
        editor.apply();
    }

    public static void saveBuildings(Context context, String building) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.key_buildings), building);
        editor.apply();
    }

    public static void saveTimeReload(Context context, String timeReload) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.key_time_reload), timeReload);
        editor.apply();
    }

    public static String getUrl(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.key_url_service), context.getString(R.string.pref_default_url_service));
    }

    public static String getBuildings(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.key_buildings), context.getString(R.string.pref_default_buildings));
    }

    public static long getTimeReload(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String s = sharedPrefs.getString(context.getString(R.string.key_time_reload), context.getString(R.string.pref_default_time_reload));

        long timeReload;

        try {
            timeReload = Long.parseLong(s);
        } catch (Exception e) {
            timeReload = 10000;
        }

        return timeReload;
    }

    public static List<Building> convertJsonToList(String json) {
        List<Building> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject room = jsonArray.getJSONObject(i);
                list.add(new Building(room.getString("name"), room.getString("parameter")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
