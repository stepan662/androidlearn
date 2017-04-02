package com.example.stepan.androidlearn;

import java.util.ArrayList;

/**
 * Created by stepan on 3/30/17.
 */

public abstract class ArrayChangedListener {
    public abstract void arrayChanged(ArrayList<FirebaseObjectInterface> array);
    public abstract void itemAdded(FirebaseObjectInterface item);
}
