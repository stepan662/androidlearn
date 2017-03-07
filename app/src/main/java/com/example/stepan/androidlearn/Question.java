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
    private ArrayList<Answer> answers;
    private Answer correctAnswer;

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
    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
        notifyPropertyChanged(BR.answers);
    }

    @Bindable
    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
        notifyPropertyChanged(BR.correctAnswer);
    }
}
