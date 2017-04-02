package com.example.stepan.androidlearn;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by stepan on 3/24/17.
 */

public class UserResultsManager {
    Query userQuery;
    Context appContext;
    FirebaseArrayListener listener;

    Boolean listening = false;
    Boolean online = false;
    String topicId = null;

    public UserResultsManager(Context appContext, FirebaseArrayListener listener) {
        this.appContext = appContext;
        this.listener = listener;
    }

    public void setListener(FirebaseArrayListener listener) {
        this.listener = listener;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicId() {
        return this.topicId;
    }

    public void setUserQuery(Query userQuery) {
        this.userQuery = userQuery;
    }

    public void setListening(Boolean newStatus) {
        if(!this.listening && newStatus) {
            this.listening = newStatus;
            Log.d("ONLINE", this.online.toString());
            if(this.online) {
                if (userQuery != null) {
                    userQuery.addChildEventListener(this.listener);
                }
            } else {
                notifyAll(getOfflineData());
            }

        } else if (this.listening && !newStatus){
            this.listening = newStatus;
            if(this.online) {
                if (userQuery != null) {
                    userQuery.removeEventListener(this.listener);
                }
            }
        }
    }

    public void goOffline() {
        this.online = false;
    }

    public void goOnline() {
        this.online = true;
    }

    public void addItem(QuizResult result, DatabaseReference reference) {
        if(online) {
            DatabaseReference newRecord = reference.push();
            newRecord.setValue(result);
        } else {
            ArrayList<QuizResult> results = getOfflineData();
            long lastId = 0;
            if(results.size() != 0) {
                lastId = Long.valueOf(results.get(results.size() - 1).getFirebaseId());
            }
            result.setFirebaseId(String.valueOf(lastId + 1));
            results.add(result);
            setOfflineData(results);
            notifyAll(results);
        }
    }

    public void notifyAll(Iterable<QuizResult> results) {
        if(listening) {
            for (QuizResult result : results) {
                if(this.topicId == null || Objects.equals(this.topicId, result.getTopicId())) {
                    listener.onChildAdded(result);
                }
            }
        }
    }

    public ArrayList<QuizResult> getOfflineData() {
        QuizResult[] resArray = (QuizResult[]) LocalStorage.loadArray(appContext, "RESULTS", QuizResult[].class);
        ArrayList<QuizResult> results = new ArrayList<>();
        if(resArray != null) {
            Collections.addAll(results, resArray);
        }
        return results;
    }

    public void setOfflineData(ArrayList<QuizResult> results) {
        LocalStorage.storeObject(this.appContext, "RESULTS", results);
    }

    public void pushOfflineToOnline(DatabaseReference reference){
        ArrayList<QuizResult> offlineData = getOfflineData();
        for(QuizResult res: getOfflineData()) {
            res.setFirebaseId(null);
            addItem(res, reference.child(res.getTopicId()));
            offlineData.remove(0);
            setOfflineData(offlineData);
        }
    }
}
