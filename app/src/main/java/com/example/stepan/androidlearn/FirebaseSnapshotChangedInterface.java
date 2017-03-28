package com.example.stepan.androidlearn;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by stepan on 3/27/17.
 */

public abstract class FirebaseSnapshotChangedInterface {
    abstract void snapshotChanged(DataSnapshot dataSnapshot);
}
