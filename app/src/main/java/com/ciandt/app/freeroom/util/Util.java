package com.ciandt.app.freeroom.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.model.Room;

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

    public static void saveRooms(Context context, String rooms) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.key_rooms), rooms);
        editor.apply();
    }

    public static String getUrl(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.key_url_service), context.getString(R.string.pref_default_url_service));
    }

    public static String getRooms(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.key_rooms), context.getString(R.string.pref_default_rooms));
    }

    public static List<Room> convertJsonToList(String json){
        List<Room> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject room = jsonArray.getJSONObject(i);
                list.add(new Room(room.getString("name"), room.getString("parameter")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
