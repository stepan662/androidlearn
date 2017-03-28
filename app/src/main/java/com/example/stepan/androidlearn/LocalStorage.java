package com.example.stepan.androidlearn;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stepan on 2/25/17.
 */

class LocalStorage {

    static public void storeObject(Context context, String key, Serializable obj) {

        String str = toJson(obj);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, str);

        editor.commit();
        Log.d("localStorageStore", key + ": " + preferences.getString(key, ""));
    }

    static public Serializable loadObject(Context context, String key, Class cls) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        String str = preferences.getString(key, "");

        Log.d("localStorageLoad", key + ": " + str);

        return fromJson(str, cls);
    }

    static public String loadString(Context context, String key) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String str = preferences.getString(key, "");
        return str;
    }

    public static Object[] loadArray(Context context, String key, Class cls) {
        String json = loadString(context, key);
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        return (Object[]) gson.fromJson(json, cls);
    }

    static public String toJson(Serializable obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    static public Serializable fromJson(String str, Class cls) {
        Gson gson = new Gson();
        return (Serializable) gson.fromJson(str, cls);
    }
}
