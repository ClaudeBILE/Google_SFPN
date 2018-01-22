package com.example.sfpn.google_sfpn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    ArrayList<Double> scoreList = new ArrayList<Double>();
    private ListView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreList.add(42.0);

        score = (ListView) findViewById(R.id.scoreListView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, scoreList);
        score.setAdapter(adapter);

    }
}
