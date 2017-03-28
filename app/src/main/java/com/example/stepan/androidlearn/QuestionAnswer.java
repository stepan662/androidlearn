package com.example.stepan.androidlearn;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by stepan on 3/11/17.
 */

public class QuestionAnswer extends BaseObservable implements Serializable {
    private String answerId;
    private String questionId;

    @Bindable
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
        notifyPropertyChanged(BR.answerId);
    }

    @Bindable
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
        notifyPropertyChanged(BR.questionId);
    }

    @Override
    public String toString() {
        return LocalStorage.toJson(this);
    }
}
