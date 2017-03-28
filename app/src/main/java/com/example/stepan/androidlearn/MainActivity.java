package com.example.stepan.androidlearn;

import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Firebase";
    private final int RC_SIGN_IN = 9876;

    private Settings settings = new Settings();

    private static FirebaseDatabase firebase;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener = null;

    private DatabaseReference topicsDataRef;
    private FirebaseArrayListener topicsDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.activity_main_action_bar);
        View view =getSupportActionBar().getCustomView();

        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance();
            firebase.setPersistenceEnabled(true);
        }

        topicsDataRef = firebase.getReference("topics");
        topicsDataRef.keepSynced(true);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else if (user == null) {
                    // User is signed out
                }
                // ...
            }
        };

        final ListView topicsListView = (ListView) findViewById(R.id.topics_list);
        try {
            topicsDataListener = new FirebaseArrayListener(TopicsAdapter.class, Topic.class, this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        topicsDataListener.setSnapshotObserver(new FirebaseSnapshotChangedInterface() {
            @Override
            void snapshotChanged(DataSnapshot dataSnapshot) {
                for(DataSnapshot question: dataSnapshot.child("questions").getChildren()){
                    Object imageUrlObj = question.child("imageUrl").getValue();
                    if(imageUrlObj != null) {
                        String imageUrl = imageUrlObj.toString();
                        StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageUrl);
                        Glide
                            .with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(ref)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(new LocalGallery(imageUrl));
                    }
                }
            }
        });

        topicsListView.setAdapter(topicsDataListener.getItemsAdapter());
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topicClicked = (Topic) topicsDataListener.getItemsAdapter().getItem(position);

                Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                intent.putExtra("TOPIC_ID", topicClicked.getFirebaseId());
                startActivity(intent);
            }
        });

        settings.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (settings.getUserIdToken() != null) {
                    firebaseAuthWithGoogle(settings.getUserIdToken());
                } else {
                    firebaseAuth.signOut();
                }
            }
        });


        settings.loadSettings(getApplicationContext());
    }

    public void goToSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("SETTINGS", LocalStorage.toJson(this.settings));
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                settings.loadSettings(getApplicationContext());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + idToken);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                }
            });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
        topicsDataRef.addChildEventListener(topicsDataListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        if (topicsDataListener != null) {
            topicsDataRef.removeEventListener(topicsDataListener);
        }
    }

}
