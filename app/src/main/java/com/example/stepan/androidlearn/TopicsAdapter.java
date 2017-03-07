package com.example.stepan.androidlearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stepan on 2/26/17.
 */

public class TopicsAdapter extends ArrayAdapter<Topic> {
    public TopicsAdapter(Context context, ArrayList<Topic> topics) {
        super(context, 0, topics);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Topic topic = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.topic_item, parent, false);
        }
        // Lookup view for data population
        TextView textViewName = (TextView) convertView.findViewById(R.id.topic_name);
        textViewName.setText(topic.getName());
        TextView textViewDesc = (TextView) convertView.findViewById(R.id.topic_description);
        textViewDesc.setText(topic.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
