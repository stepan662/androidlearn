package com.example.stepan.androidlearn;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.stepan.androidlearn.databinding.ActivityTopicBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class TopicActivity extends AppCompatActivity {

    private static final int RC_QUIZ = 9876;
    private static FirebaseDatabase firebase;

    private DatabaseReference topicDataRef;
    private FirebaseArrayListener topicsDataListener;
    private String topicId;
    private TopicComplete topicObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.activity_topic_action_bar);
        final View view = getSupportActionBar().getCustomView();

        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance();
        }

        topicId = getIntent().getStringExtra("TOPIC_ID");

        topicDataRef = firebase.getReference("topics").child(topicId);

        final ActivityTopicBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_topic);
        binding.setTopic(this.topicObject);

        topicDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TopicActivity.this.topicObject = (TopicComplete) dataSnapshot.getValue(TopicComplete.class);
                binding.setTopic(topicObject);
                TextView textView = (TextView)view.findViewById(R.id.topic_name);
                textView.setText(topicObject.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void goBack(View v) {
        this.finish();
    }

    public void startQuiz(View v) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("TOPIC", LocalStorage.toJson(this.topicObject));
        startActivityForResult(intent, RC_QUIZ);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }
}
