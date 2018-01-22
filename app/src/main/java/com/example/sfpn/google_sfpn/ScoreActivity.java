package com.example.sfpn.google_sfpn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class ScoreActivity extends Activity {
    ArrayList<Score> list = new ArrayList<Score>();
    private ListView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        list.add(new Score(666.666, 2));
        list.add(new Score(3.9,0));


        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        ScoresAdapter adapter = new ScoresAdapter(this, list);

        score = (ListView) findViewById(R.id.scoreListView);
        score.setAdapter(adapter);

    }

}
