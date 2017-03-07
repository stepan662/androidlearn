package com.example.stepan.androidlearn;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stepan on 2/27/17.
 */

public class FirebaseArrayListener implements ChildEventListener {

    final static String TAG = "FIREBASE OBSERVER";

    private HashMap<String, Integer> itemsMap = new HashMap<String, Integer>();
    private ArrayList<FirebaseObjectInterface> itemsList;
    private ArrayAdapter<FirebaseObjectInterface> itemsAdapter;
    Class itemClass;

    FirebaseArrayListener(Class adapterClass, Class itemClass, Context context) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        itemsList = new ArrayList<>();

        itemsAdapter = (ArrayAdapter<FirebaseObjectInterface>) adapterClass.getConstructor(Context.class, ArrayList.class)
                .newInstance(context, itemsList);

        this.itemClass = itemClass;
    }


    public ArrayList<FirebaseObjectInterface> getItemsList() {
        return itemsList;
    }

    public void setItemsList(ArrayList<FirebaseObjectInterface> itemsList) {
        this.itemsList = itemsList;
    }

    public ArrayAdapter<FirebaseObjectInterface> getItemsAdapter() {
        return itemsAdapter;
    }

    public void setItemsAdapter(ArrayAdapter<FirebaseObjectInterface> itemsAdapter) {
        this.itemsAdapter = itemsAdapter;
    }



    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (!itemsMap.containsKey(dataSnapshot.getKey())) {
            FirebaseObjectInterface newItem = (FirebaseObjectInterface) dataSnapshot.getValue(itemClass);
            newItem.setFirebaseId(dataSnapshot.getKey());
            itemsMap.put(dataSnapshot.getKey(), itemsAdapter.getCount());
            itemsAdapter.add(newItem);
        }
    }

    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        int topicNumber = itemsMap.get(dataSnapshot.getKey());
        FirebaseObjectInterface newItem = (FirebaseObjectInterface) dataSnapshot.getValue(itemClass);
        itemsList.set(topicNumber, newItem);
        itemsAdapter.notifyDataSetChanged();
    }

    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d(TAG, dataSnapshot.toString());
    }

    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, dataSnapshot.toString());
    }

    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, databaseError.toString());
    }
}
