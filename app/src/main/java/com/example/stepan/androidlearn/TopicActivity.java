package com.example.stepan.androidlearn;

import android.content.Intent;
import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.stepan.androidlearn.databinding.ActivityTopicBinding;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TopicActivity extends AppCompatActivity {

    private static final int RC_QUIZ = 9876;
    private static FirebaseDatabase firebase;

    private DatabaseReference topicDataRef;
    private ValueEventListener topicsDataListener;
    private String topicId;
    private Topic topicObject;

    private Query resultsDataRef;
    private FirebaseArrayListener resultsListener;
    private UserResultsManager resultsManager;

    private Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.activity_topic_action_bar);
        final View view = getSupportActionBar().getCustomView();

        this.settings = new Settings();
        this.settings.loadSettings(this.getApplicationContext());


        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance();
        }

        topicId = getIntent().getStringExtra("TOPIC_ID");

        topicDataRef = firebase.getReference("topics").child(topicId);

        final ActivityTopicBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_topic);
        binding.setTopic(this.topicObject);

        topicsDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                topicObject = Topic.loadFromSnapshot(dataSnapshot, getApplicationContext());

                binding.setTopic(topicObject);
                TextView textView = (TextView)view.findViewById(R.id.topic_name);
                textView.setText(topicObject.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Log.d("TopicID", this.topicId);

        ListView resultsView = (ListView) binding.getRoot().findViewById(R.id.results);
        try {
            resultsListener = new FirebaseArrayListener(ResultsAdapter.class, QuizResult.class, this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultsListener.setSortFunction(new Comparator<FirebaseObjectInterface>() {
            @Override
            public int compare(FirebaseObjectInterface lhs, FirebaseObjectInterface rhs) {
                QuizResult l = (QuizResult)lhs;
                QuizResult r = (QuizResult)rhs;
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z", Locale.UK);
                Date ldate;
                Date rdate;
                try {
                    ldate = format.parse(l.getTime());
                    rdate = format.parse(r.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
                return rdate.compareTo(ldate);
            }
        });
        resultsView.setAdapter(resultsListener.getItemsAdapter());

        this.resultsManager = new UserResultsManager(this.getApplicationContext(), resultsListener);
        this.resultsManager.setTopicId(this.topicId);

        if(settings.getUserIdToken() != null) {
            // use firebase
            resultsDataRef = firebase.getReference("users").child(settings.getUserIdToken())
                    .orderByChild("topicId").startAt(this.topicId).endAt(this.topicId);
            resultsManager.setUserQuery(resultsDataRef);
            resultsManager.goOnline();
        } else {
            // use local storage
            resultsManager.goOffline();
        }
    }

    public void goBack(View v) {
        this.finish();
    }

    public void startQuiz(View v) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("QUESTIONS", randomQuestions(this.topicObject.getQuestionArray().size() / 2));
        intent.putExtra("TOPIC_ID", this.topicObject.getFirebaseId());
        startActivityForResult(intent, RC_QUIZ);
    }

    private ArrayList<Question> randomQuestions(int count){
        ArrayList<Question> unpicked = new ArrayList<>(this.topicObject.getQuestionArray());
        ArrayList<Question> picked = new ArrayList<>();

        Random rn = new Random();

        for(int i = 0; i < count && unpicked.size() > 0; i++) {
            int randomIndex = rn.nextInt(unpicked.size());
            Question q = unpicked.get(randomIndex);
            unpicked.remove(randomIndex);
            picked.add(q);
        }
        return picked;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_QUIZ) {
            if (resultCode == RESULT_OK) {
                String answers = data.getStringExtra("ANSWERS");
                Log.d("Final answers", answers);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        topicDataRef.addValueEventListener(topicsDataListener);
        resultsManager.setListening(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (topicsDataListener != null) {
            topicDataRef.removeEventListener(topicsDataListener);
        }
        resultsManager.setListening(false);
    }
}
