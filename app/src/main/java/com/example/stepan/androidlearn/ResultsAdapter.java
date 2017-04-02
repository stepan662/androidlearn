package com.example.stepan.androidlearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stepan on 3/21/17.
 */

public class ResultsAdapter extends ArrayAdapter<QuizResult> {

    public ResultsAdapter(Context context, ArrayList<QuizResult> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        QuizResult result = getItem(this.getCount() - (position + 1));
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_item, parent, false);
        }
        // Lookup view for data population
        TextView scoreView = (TextView) convertView.findViewById(R.id.result_score);
        String scoreText = "";
        for(int i = 0; i < result.getQuestionCount(); i++) {
            if(i < result.getCorrectCount()) {
                scoreText += "★";
            } else {
                scoreText += "☆";
            }
        }

        scoreView.setText(scoreText);

        Date date = new Date(result.getTime());


        RelativeTimeTextView dateView = (RelativeTimeTextView) convertView.findViewById(R.id.result_date);
        dateView.setReferenceTime(date.getTime());

        // Return the completed view to render on screen
        return convertView;
    }

}
