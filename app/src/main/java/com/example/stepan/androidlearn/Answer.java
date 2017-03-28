package com.example.stepan.androidlearn;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by stepan on 2/26/17.
 */

public class Answer extends BaseObservable implements Serializable{
    private String text;
    private String fireabseId;

    public Answer() {

    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public String getFirebaseId() {
        return this.fireabseId;
    }

    public void setFirebaseId(String id) {
        this.fireabseId = id;
        this.notifyPropertyChanged(BR.firebaseId);
    }
}
