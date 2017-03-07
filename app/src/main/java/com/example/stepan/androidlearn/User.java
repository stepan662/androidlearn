package com.example.stepan.androidlearn;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by stepan on 2/23/17.
 */

public class User extends BaseObservable {
    private String firstName = null;
    private String lastName = null;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public void set(User user) {
        this.setFirstName(user.firstName);
        this.setLastName(user.lastName);
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}
