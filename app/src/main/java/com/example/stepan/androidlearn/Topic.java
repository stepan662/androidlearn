package com.example.stepan.androidlearn;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stepan on 2/23/17.
 */

public class Topic extends BaseObservable implements Serializable, FirebaseObjectInterface {

    private String id = null;
    private String name = null;
    private String description = null;
    private String fireabseId = null;

    public Topic() {
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Override
    public String toString() {
        return "<" + this.name + ">";
    }

    @Bindable
    public String getFirebaseId() {
        return this.fireabseId;
    }

    public void setFirebaseId(String id) {
        this.fireabseId = id;
        this.notifyPropertyChanged(BR.firebaseId);
    }

    private ArrayList<Question> questionArray = new ArrayList<Question>();

    @Bindable
    public ArrayList<Question> getQuestionArray() {
        return questionArray;
    }

    public void setQuestionArray(ArrayList<Question> questionArray) {
        this.questionArray = questionArray;
        notifyPropertyChanged(BR.questionArray);
    }

    static Topic loadFromSnapshot(DataSnapshot dataSnapshot, Context context) {
        Topic topicObject = (Topic) dataSnapshot.getValue(Topic.class);
        topicObject.setFirebaseId(dataSnapshot.getKey());

        DataSnapshot questionsSnapshot = dataSnapshot.child("questions");
        for(DataSnapshot questionSnapshot: questionsSnapshot.getChildren()) {
            Question question = (Question) questionSnapshot.getValue(Question.class);
            question.setFirebaseId(questionSnapshot.getKey());

            Log.d("Topic activity", "onDataChange: " + questionSnapshot.toString());

            DataSnapshot answersSnapshot = questionSnapshot.child("answers");
            for(DataSnapshot answerSnapshot: answersSnapshot.getChildren()) {
                Answer answer = answerSnapshot.getValue(Answer.class);
                answer.setFirebaseId(answerSnapshot.getKey());
                question.getAnswerArray().add(answer);
            }

            if(question.getImageUrl() != null && !question.getImageUrl().equals("")) {
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(question.getImageUrl());

                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(ref);
            }

            topicObject.getQuestionArray().add(question);
        }
        return topicObject;
    }
}

