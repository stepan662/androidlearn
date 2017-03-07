package com.example.stepan.androidlearn;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by stepan on 2/25/17.
 */


class StoredValue {
    public AppCompatActivity activity;
    public String key;

    StoredValue(AppCompatActivity activity, String key) {
        this.activity = activity;
        this.key = key;
    }
}


class LocalStorage {

    static public void storeObject(AppCompatActivity activity, String key, Serializable obj) {

        String str = toJson(obj);

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(key, str);

        editor.commit();
        Log.d("localStorageStore", key + ": " + sharedPref.getString(key, ""));
    }

    static public Serializable loadObject(AppCompatActivity activity, String key, Class cls) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String str = sharedPref.getString(key, "");

        Log.d("localStorageLoad", key + ": " + str);

        return fromJson(str, cls);
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
