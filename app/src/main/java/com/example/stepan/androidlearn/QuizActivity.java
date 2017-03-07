package com.example.stepan.androidlearn;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.stepan.androidlearn.databinding.ActivityTopicBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by stepan on 3/5/17.
 */

public class QuizActivity extends FragmentActivity {

    private QuestionPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;


    private static FirebaseDatabase firebase;
    private DatabaseReference topicDataRef;
    private TopicComplete topic;
    private String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance();
        }

        this.topic = (TopicComplete) LocalStorage.fromJson(getIntent().getStringExtra("TOPIC"), TopicComplete.class);

        Log.d("Quiz", this.topic.getQuestions().toString());

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (QuestionPager) findViewById(R.id.quiz_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this.topic);
        mPager.setAdapter(mPagerAdapter);

        mPager.setAllowedSwipeDirection(SwipeDirection.none);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private TopicComplete myTopic;

        public ScreenSlidePagerAdapter(FragmentManager fm, TopicComplete topic) {
            super(fm);
            this.myTopic = topic;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("QUESTION", LocalStorage.toJson(topic.getQuestions().get(position)));
            bundle.putInt("QUESTION_NUMBER", position);
            bundle.putInt("QUESTIONS_COUNT", this.getCount());
            QuizSlidePagerFragment page = new QuizSlidePagerFragment();
            page.setArguments(bundle);
            return page;
        }

        @Override
        public int getCount() {
            if (myTopic != null) {
                return myTopic.getQuestions().size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void nextQuestion(View v) {
        Log.d("Quiz", "next clicked ...");
        int current = mPager.getCurrentItem();
        if (current != mPagerAdapter.getCount()-1) {
            mPager.setCurrentItem(current + 1);
        } else {
            finish();
        }
    }
}
