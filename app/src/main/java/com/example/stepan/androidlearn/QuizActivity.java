package com.example.stepan.androidlearn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by stepan on 3/5/17.
 */

public class QuizActivity extends AppCompatActivity {

    private static String TAG = "QUIZ";

    private QuestionPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    private String topicId;
    private ArrayList<Question> questions;
    private SeekBar seekBar;
    private TextView questionActionBar;
    private ArrayList<QuestionAnswer> answers;
    private Boolean finished;

    private Settings settings = new Settings();

    private UserResultsManager userResultsManager;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        this.finished = getIntent().getBooleanExtra("FINISHED", false);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.activity_question_action_bar);
        final View view = getSupportActionBar().getCustomView();

        questionActionBar = (TextView)view.findViewById(R.id.question_name);

        this.topicId = getIntent().getStringExtra("TOPIC_ID");
        this.questions = (ArrayList<Question>) getIntent().getSerializableExtra("QUESTIONS");
        String answersString = getIntent().getStringExtra("ANSWERS");
        answers = new ArrayList<>();
        if(answersString != null) {
            answers = new Gson().fromJson(answersString, new TypeToken<List<QuestionAnswer>>(){}.getType());
        }


        questionActionBar.setText("Question 1 of " + String.valueOf(this.questions.size()));

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        seekBar.setMax((this.questions.size()) * 100);
        seekBar.setProgress(0);

        // Instantiate a ViewPager and a PagerAdapter
        mPager = (QuestionPager) findViewById(R.id.quiz_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        if(this.finished) {
            mPager.setAllowedSwipeDirection(SwipeDirection.all);
        } else {
            mPager.setAllowedSwipeDirection(SwipeDirection.none);
        }

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                seekBar.setProgress((int)Math.round((position + positionOffset) * 100.0));
            }

            @Override
            public void onPageSelected(int position) {
                if (position == questions.size()){
                    questionActionBar.setText("Results");
                } else {
                    questionActionBar.setText("Question " + String.valueOf(position + 1) + " of "
                            + String.valueOf(questions.size()));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        settings.loadSettings(getApplicationContext());

        this.userResultsManager = new UserResultsManager(getApplicationContext(), null);
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

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position != getCount() - 1) {
                Bundle bundle = new Bundle();
                bundle.putString("QUESTION", LocalStorage.toJson(questions.get(position)));
                bundle.putInt("QUESTION_NUMBER", position);
                bundle.putInt("QUESTIONS_COUNT", this.getCount());
                bundle.putSerializable("QUESTION_ANSWER", answers.size() > position ? answers.get(position): null);
                bundle.putBoolean("FINISHED", finished);
                QuizSlidePagerFragment page = new QuizSlidePagerFragment();
                page.setArguments(bundle);
                return page;
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("RESULT", (QuizResult)getIntent().getSerializableExtra("RESULT"));
                bundle.putBoolean("FINISHED", finished);
                ResultFragment resultFragment = new ResultFragment();
                resultFragment.setArguments(bundle);
                return resultFragment;
            }
        }

        @Override
        public int getCount() {
            if (questions != null) {
                return questions.size() + 1;
            } else {
                return 0;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        private Fragment mCurrentFragment;

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }
    }

    public void goBack(View v) {
        this.onBackPressed();
    }

    public void nextQuestion(View v) throws JSONException {
        int current = mPager.getCurrentItem();
        QuizSlidePagerFragment page = (QuizSlidePagerFragment) mPagerAdapter.getCurrentFragment();
        QuestionAnswer answer = page.getAnswer();
        if (answer.getAnswerId() == null) {
            return;
        }

        if(current == answers.size()) {
            this.answers.add(page.getAnswer());
            getIntent().putExtra("ANSWERS", LocalStorage.toJson(this.answers));
            if (current < mPagerAdapter.getCount() - 2 ) {
                mPager.setCurrentItem(current + 1);
            } else if (current == mPagerAdapter.getCount() - 2 ) {
                this.finished = true;
                QuizResult result = generateResult();
                getIntent().putExtra("FINISHED", true);
                getIntent().putExtra("RESULT", result);
                DatabaseReference ref = null;
                if(this.settings.getUserIdToken() != null) {
                    ref = FirebaseDatabase.getInstance().getReference("users").child(settings.getUserIdToken());
                    userResultsManager.goOnline();
                }
                userResultsManager.addItem(result, ref);
                mPagerAdapter.notifyDataSetChanged();
                mPager.setAllowedSwipeDirection(SwipeDirection.all);
                mPager.setCurrentItem(current + 1);
            }
        }
    }

    private QuizResult generateResult() {
        ArrayList<Question> questions = this.questions;
        QuizResult result = new QuizResult();
        int questionCorrect = 0;

        for(Question question: questions) {
            for(QuestionAnswer questionAnswer: answers) {
                if(Objects.equals(question.getFirebaseId(), questionAnswer.getQuestionId()) &&
                        (Objects.equals(question.getCorrectAnswerId(), questionAnswer.getAnswerId()))) {
                    questionCorrect++;
                    break;
                }
            }
        }

        result.setQuestionCount(questions.size());
        result.setCorrectCount(questionCorrect);
        result.setTime((new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z", Locale.UK)).format(new Date()));
        result.setTopicId(this.topicId);

        return result;
    }
}
