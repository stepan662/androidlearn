package com.example.stepan.androidlearn;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by stepan on 3/24/17.
 */

public interface CustomChildEventListener {
    void onChildAdded(FirebaseObjectInterface var1);

    void onChildChanged(FirebaseObjectInterface var1);

    void onChildRemoved(FirebaseObjectInterface var1);

    void onChildMoved(FirebaseObjectInterface var1);

    void onCancelled(FirebaseObjectInterface var1);
}

