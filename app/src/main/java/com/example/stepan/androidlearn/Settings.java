package com.example.stepan.androidlearn;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Created by stepan on 2/25/17.
 */

public class Settings extends BaseObservable implements Serializable {
    private String userIdToken = null;

    public Settings() {
        this.setUserIdToken(null);
    }

    public Settings(String userIdToken) {
        this.setUserIdToken(userIdToken);
    }

    public void set(String userIdToken) {
        this.setUserIdToken(userIdToken);
    }

    public void set(Settings settings) {
        this.setUserIdToken(settings.userIdToken);
    }

    @Bindable
    public String getUserIdToken() {
        return userIdToken;
    }

    public void setUserIdToken(String userIdToken) {
        this.userIdToken = userIdToken;
        notifyPropertyChanged(BR.userIdToken);
    }

    public void loadSettings(Context context) {
        Settings s = (Settings)LocalStorage.loadObject(context, "SETTINGS", Settings.class);
        if(s != null && !Objects.equals(s.getUserIdToken(), this.getUserIdToken())) {
            this.setUserIdToken(s.getUserIdToken());
        }
    }

    public void storeSettings(Context context) {
        LocalStorage.storeObject(context, "SETTINGS", this);
    }
}
