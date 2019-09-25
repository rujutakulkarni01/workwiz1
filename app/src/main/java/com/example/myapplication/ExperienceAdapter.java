package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExperienceAdapter extends ArrayAdapter<userExperience> {
    public ExperienceAdapter(Context context, List<userExperience>object){
        super(context,0,object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.list_item,parent,false);
        }

        TextView company = convertView.findViewById(R.id.tvCompany);
        TextView title = convertView.findViewById(R.id.tvTitle);
        TextView date = convertView.findViewById(R.id.tvDate);

        userExperience experience = getItem(position);
        company.setText(experience.getCompany());
        title.setText(experience.getTitle());
        date.setText(experience.getStart()+"-"+experience.getEnd());

        return convertView;
    }
}
