package com.example.stepan.androidlearn;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

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

    @Override
    public void setFirebaseId(String id) {
        this.fireabseId = id;
    }

    @Override
    public String getFirebaseId() {
        return this.fireabseId;
    }
}

