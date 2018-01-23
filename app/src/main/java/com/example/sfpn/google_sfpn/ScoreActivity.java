package com.example.sfpn.google_sfpn;

import android.app.Activity;
import android.app.IntentService;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class ScoreActivity extends Activity {

    private ListView score;
    ScoresAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        ScoreDataBase databaseHelper = ScoreDataBase.getInstance(this);
        ArrayList<Score> list = databaseHelper.getAllScores();

        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter = new ScoresAdapter(this, list);

        score = (ListView) findViewById(R.id.scoreListView);
        score.setAdapter(adapter);

    }

    public void addUser(int score, int lvl){
        adapter.add(new Score(score,lvl));
    }

}
