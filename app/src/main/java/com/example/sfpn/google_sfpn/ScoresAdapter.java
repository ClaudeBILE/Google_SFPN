package com.example.sfpn.google_sfpn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alexis on 22/01/18.
 */

public class ScoresAdapter extends ArrayAdapter<Score> {

    public ScoresAdapter(Context context, ArrayList<Score> scores){
        super(context,0,scores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){


        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_layout, parent, false);
        }

        Score score = getItem(position);

        TextView tvDistance = (TextView) convertView.findViewById(R.id.score);
        TextView tvNiveau = (TextView) convertView.findViewById(R.id.lvl);
        TextView tvToday = (TextView) convertView.findViewById(R.id.date);

        tvDistance.setText(score.getPoints());
        tvNiveau.setText(score.getNiveau());
        tvToday.setText(score.getTimeStamp());

        return convertView;

    }
}
