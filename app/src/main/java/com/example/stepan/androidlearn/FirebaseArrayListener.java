package com.example.stepan.androidlearn;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by stepan on 2/27/17.
 */

public class FirebaseArrayListener implements ChildEventListener, CustomChildEventListener {

    final static String TAG = "FIREBASE OBSERVER";

    private HashMap<String, FirebaseObjectInterface> itemsMap = new HashMap<String, FirebaseObjectInterface>();
    private ArrayList<FirebaseObjectInterface> itemsList;
    private ArrayAdapter<FirebaseObjectInterface> itemsAdapter;
    private Class itemClass;

    private FirebaseSnapshotChangedInterface snapshotObserver;
    private ArrayList<ArrayChangedListener> customListeners;

    FirebaseArrayListener(Class adapterClass, Class itemClass, Context context) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        itemsList = new ArrayList<>();

        itemsAdapter = (ArrayAdapter<FirebaseObjectInterface>) adapterClass.getConstructor(Context.class, ArrayList.class)
                .newInstance(context, itemsList);

        this.itemClass = itemClass;

        customListeners = new ArrayList<>();
    }

    public void setSnapshotObserver(FirebaseSnapshotChangedInterface snapshotObserver) {
        this.snapshotObserver = snapshotObserver;
    }

    public void removeCustomEventListener(ArrayChangedListener customChildEventListener) {
        this.customListeners.remove(customChildEventListener);
    }

    public void addCustomEventListener(ArrayChangedListener customListener) {
        this.customListeners.add(customListener);
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
        FirebaseObjectInterface newItem = (FirebaseObjectInterface) dataSnapshot.getValue(itemClass);
        newItem.setFirebaseId(dataSnapshot.getKey());
        this.onChildAdded(newItem);
        if(snapshotObserver != null) {
            snapshotObserver.snapshotChanged(dataSnapshot);
        }
    }

    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        FirebaseObjectInterface newItem = (FirebaseObjectInterface) dataSnapshot.getValue(itemClass);
        newItem.setFirebaseId(dataSnapshot.getKey());
        this.onChildChanged(newItem);
        if(snapshotObserver != null) {
            snapshotObserver.snapshotChanged(dataSnapshot);
        }
    }

    public void onChildRemoved(DataSnapshot dataSnapshot) {
        FirebaseObjectInterface item = itemsMap.get(dataSnapshot.getKey());
        item.setFirebaseId(dataSnapshot.getKey());
        this.onChildRemoved(item);
    }

    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, dataSnapshot.toString());
    }

    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, databaseError.toString());
    }

    @Override
    public void onChildAdded(FirebaseObjectInterface newItem) {
        if (itemsMap.containsKey(newItem.getFirebaseId())) {
            int index = itemsList.indexOf(itemsMap.get(newItem.getFirebaseId()));
            itemsList.set(index, newItem);
            itemsMap.put(newItem.getFirebaseId(), newItem);
            itemsAdapter.notifyDataSetChanged();
            for(ArrayChangedListener item: this.customListeners) {
                item.arrayChanged(itemsList);
            }
        } else {
            itemsList.add(newItem);
            itemsMap.put(newItem.getFirebaseId(), newItem);
            itemsAdapter.notifyDataSetChanged();
            for(ArrayChangedListener item: this.customListeners) {
                item.itemAdded(newItem);
            }
        }
    }

    @Override
    public void onChildChanged(FirebaseObjectInterface item) {
        FirebaseObjectInterface oldItem = itemsMap.get(item.getFirebaseId());
        itemsList.remove(oldItem);
        itemsList.add(item);

        itemsMap.put(item.getFirebaseId(), item);

        itemsAdapter.notifyDataSetChanged();
        for(ArrayChangedListener it: this.customListeners) {
            it.arrayChanged(itemsList);
        }
    }

    @Override
    public void onChildRemoved(FirebaseObjectInterface item) {
        FirebaseObjectInterface oldItem = itemsMap.get(item.getFirebaseId());

        itemsList.remove(oldItem);
        itemsAdapter.remove(oldItem);
        itemsMap.remove(oldItem);
        for(ArrayChangedListener it: this.customListeners) {
            it.arrayChanged(itemsList);
        }
    }

    @Override
    public void onChildMoved(FirebaseObjectInterface var1) {

    }

    @Override
    public void onCancelled(FirebaseObjectInterface var1) {
    }

    public void clearList() {
        this.itemsList.clear();
        this.itemsMap.clear();
        itemsAdapter.notifyDataSetChanged();
    }
}
