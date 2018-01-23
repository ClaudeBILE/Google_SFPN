package com.example.sfpn.google_sfpn;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Comparator;


public class ScoreActivity extends AppCompatActivity {

    private ListView score;
    ScoresAdapter adapter;
    ArrayList<Score> list;
    private int sortby = 0;
    private int sortdirection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        ScoreDataBase databaseHelper = ScoreDataBase.getInstance(this);
        list = databaseHelper.getAllScores();

        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter = new ScoresAdapter(this, list);

        score = (ListView) findViewById(R.id.scoreListView);
        score.setAdapter(adapter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void addUser(int score, int lvl){
        adapter.add(new Score(score,lvl));
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.up :
                sortdirection = 1;
                break;
            case R.id.down :
                sortdirection = -1;
                break;
            case R.id.sort_score :
                sortby = 0;
                break;
            case R.id.sort_lvl :
                sortby = 1;
                break;
            case R.id.sort_time :
                sortby = 2;
                break;
            default:
                break;
        }
        sortListView(sortby,sortdirection);
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    public void sortListView(final int id, final int upOrDown){
        // id : 0 = points, 1 = lvl and 2 = time
        // upOrDown : 1 = up and -1 = down;

        list.sort(new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                switch (id){
                    case 0:
                        if (o1.getPointsInt() >= o2.getPointsInt() ){
                            return -1*upOrDown;
                        }
                        return upOrDown;

                    case 1:
                        if (o1.getNiveauInt() >= o2.getPointsInt()){
                            return -1*upOrDown;
                        }
                        return upOrDown;
                    default:
                        if (o2.getTimestampDate().after(o1.getTimestampDate())){
                            return -1*upOrDown;
                        }
                        return upOrDown;
                }
            }
        });
    }
}
