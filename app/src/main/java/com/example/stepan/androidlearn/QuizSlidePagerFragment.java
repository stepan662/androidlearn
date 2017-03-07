package com.example.stepan.androidlearn;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by stepan on 3/5/17.
 */

public class QuizSlidePagerFragment extends Fragment {

    private Question question;
    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(
                R.layout.quiz_screen_fragment, container, false);

        this.question = (Question) LocalStorage.fromJson(getArguments().getString("QUESTION"), Question.class);

        bindQuestion();

        return rootView;
    }

    private void bindQuestion() {
        if(this.question != null && this.rootView != null) {
            TextView textView = (TextView) rootView.findViewById(R.id.quiestion_text);
            textView.setText(this.question.getText());
        }
    }
}
