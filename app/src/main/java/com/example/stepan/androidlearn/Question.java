package com.example.stepan.androidlearn;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stepan on 2/26/17.
 */

public class Question extends BaseObservable implements Serializable{
    private String text;
    private String imageUrl;
    private String correctAnswerId;
    private String fireabseId;
    private ArrayList<Answer> answerArray = new ArrayList<Answer>();

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    @Bindable
    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    public void setCorrectAnswerId(String correctAnswerId) {
        this.correctAnswerId = correctAnswerId;
        notifyPropertyChanged(BR.correctAnswerId);
    }

    @Bindable
    public String getFirebaseId() {
        return this.fireabseId;
    }

    public void setFirebaseId(String id) {
        this.fireabseId = id;
        this.notifyPropertyChanged(BR.firebaseId);
    }

    @Bindable
    public ArrayList<Answer> getAnswerArray() {
        return answerArray;
    }

    public void setAnswerArray(ArrayList<Answer> answerArray) {
        this.answerArray = answerArray;
        notifyPropertyChanged(BR.answerArray);
    }

    @Override
    public String toString() {
        return LocalStorage.toJson(this);
    }

}
