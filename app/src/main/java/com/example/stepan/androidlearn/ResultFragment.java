package com.example.stepan.androidlearn;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stepan.androidlearn.databinding.FragmentResultBinding;

import java.util.ArrayList;
import java.util.Objects;


public class ResultFragment extends Fragment {

    private QuizResult result;
    private FragmentResultBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d("ResultFragment", "onCreateView");


        result = (QuizResult) getArguments().getSerializable("RESULT");


        binding = FragmentResultBinding.inflate(inflater, container, false);
        updateView();

        return binding.getRoot();
    }

    private void updateView() {
        if (result == null)
            return;

        String stars = "";
        for(int i = 0; i < result.getQuestionCount(); i++) {
            if(i < result.getCorrectCount()) {
                stars += "★";
            } else {
                stars += "☆";
            }
        }
        binding.setStars(stars);
    }
}
