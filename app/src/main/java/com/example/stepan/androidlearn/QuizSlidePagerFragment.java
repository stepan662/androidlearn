package com.example.stepan.androidlearn;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.stepan.androidlearn.databinding.QuizScreenFragmentBinding;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by stepan on 3/5/17.
 */

public class QuizSlidePagerFragment extends Fragment {

    private Question question;
    private int questionNumber;
    private int questionCount;
    private Boolean finished;
    private QuestionAnswer answer = new QuestionAnswer();
    private View view;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //probably orientation change
            QuestionAnswer a = (QuestionAnswer) savedInstanceState.getSerializable("ANSWER");
            if (a != null) {
                Log.d("FRAGMENT", "restored" + a.toString());
                answer.setAnswerId(a.getAnswerId());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.question = (Question) LocalStorage.fromJson(getArguments().getString("QUESTION"), Question.class);
        this.questionNumber = (int) getArguments().getInt("QUESTION_NUMBER");
        this.questionCount = (int) getArguments().getInt("QUESTIONS_COUNT");
        this.finished = getArguments().getBoolean("FINISHED");

        QuestionAnswer previousAnswer = (QuestionAnswer) getArguments().getSerializable("QUESTION_ANSWER");
        if (previousAnswer != null) {
            answer.setAnswerId(previousAnswer.getAnswerId());
        }

        answer.setQuestionId(question.getFirebaseId());

        QuizScreenFragmentBinding binding = QuizScreenFragmentBinding.inflate(inflater, container, false);
        binding.setQuestion(this.question);
        binding.setQuestionNumber(this.questionNumber);
        binding.setQuestionsCount(this.questionCount);
        binding.setAnswer(this.answer);
        binding.setFin(this.finished);

        view = binding.getRoot();

        String imageUrl = question.getImageUrl();
        if(imageUrl != null && !imageUrl.equals("")) {
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageUrl);

            Glide
                .with(getActivity().getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

        }

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.answers_group);

        float density = getResources().getDisplayMetrics().density;


        RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );

        int margin = (int)(10*density);
        params_rb.setMargins(margin, margin, margin, margin);

        for(Answer answer: this.question.getAnswerArray()) {
            RadioButton radio = new RadioButton(this.getContext());
            radio.setText(answer.getText());
            radio.setId(this.question.getAnswerArray().indexOf(answer));
            if(this.finished) {
                Log.d("asdfasdf", answer.getFirebaseId() + ", " + this.answer.getAnswerId());
                if(Objects.equals(answer.getFirebaseId(), this.question.getCorrectAnswerId())) {
                    radio.setTextColor(Color.parseColor("#4CAF50"));
                }else if(Objects.equals(answer.getFirebaseId(), this.answer.getAnswerId())) {
                    radio.setTextColor(Color.parseColor("#F44336"));
                }
            }
            if(this.finished) {
                radio.setClickable(false);
            }
            radioGroup.addView(radio, params_rb);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int index) {
            if(index >= 0) {
                Answer checkedAnswer = question.getAnswerArray().get(index);
                answer.setAnswerId(checkedAnswer.getFirebaseId());
            }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ANSWER", answer);
    }

    public QuestionAnswer getAnswer() {
        return this.answer;
    }
}
