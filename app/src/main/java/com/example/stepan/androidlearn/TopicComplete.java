package com.example.stepan.androidlearn;

import android.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;

public class TopicComplete extends Topic implements Serializable{
    private ArrayList<Question> questions;

    @Bindable
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
        notifyPropertyChanged(BR.questions);
    }
}
