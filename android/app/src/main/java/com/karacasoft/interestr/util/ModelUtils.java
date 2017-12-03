package com.karacasoft.interestr.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for network models.
 *
 * Created by karacasoft on 3.12.2017.
 */

public class ModelUtils {

    public static final String TAG = "ModelUtils";

    public static int jsonGetIntOrDefault(JSONObject obj, String key, int defaultValue) {
        try {
            return obj.getInt(key);
        } catch (JSONException e) {
            Log.w(TAG, "Key \"" + key + "\" not found in object, default value used");
            return defaultValue;
        }
    }

    public static String jsonGetStringOrDefault(JSONObject obj, String key, String defaultValue) {
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            Log.w(TAG, "Key \"" + key + "\" not found in object, default value used");
            return defaultValue;
        }
    }

    public static JSONObject jsonGetJSONObjectOrDefault(JSONObject obj, String key, JSONObject defaultValue) {
        try {
            return obj.getJSONObject(key);
        } catch (JSONException e) {
            Log.w(TAG, "Key \"" + key + "\" not found in object, default value used");
            return defaultValue;
        }
    }

    public static JSONArray jsonGetJSONArrayOrDefault(JSONObject obj, String key, JSONArray defaultValue) {
        try {
            return obj.getJSONArray(key);
        } catch (JSONException e) {
            Log.w(TAG, "Key \"" + key + "\" not found in object, default value used");
            return defaultValue;
        }
    }

    public static Date jsonGetDate(JSONObject obj, String key) throws JSONException, ParseException {
        String date = obj.getString(key);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        return df.parse(date);
    }

}
