package com.example.stepan.androidlearn;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by stepan on 3/21/17.
 */

public class QuizResult extends BaseObservable implements Serializable, FirebaseObjectInterface{
    private String time;
    private String topicId;
    private int questionCount;
    private int correctCount;
    private String firebaseId;

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
        notifyPropertyChanged(BR.topicId);
    }

    @Bindable
    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
        notifyPropertyChanged(BR.questionCount);
    }

    @Bindable
    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
        notifyPropertyChanged(BR.correctCount);
    }

    @Override
    public String toString() {
        return LocalStorage.toJson(this);
    }

    @Override
    public void setFirebaseId(String id) {
        firebaseId = id;
    }

    @Override
    public String getFirebaseId() {
        return firebaseId;
    }
}
