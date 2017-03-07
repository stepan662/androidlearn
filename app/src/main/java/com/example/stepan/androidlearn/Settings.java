package com.example.stepan.androidlearn;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.*;

import java.io.Serializable;

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

}
