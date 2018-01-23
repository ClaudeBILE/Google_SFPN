package com.example.sfpn.google_sfpn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.PopupMenu;
import android.widget.ShareActionProvider;

import java.util.ArrayList;
import java.util.Comparator;


public class ScoreActivity extends AppCompatActivity {

    private ListView score;
    ScoresAdapter adapter;
    ArrayList<Score> list;
    ScoreDataBase databaseHelper;
    private int sortby = 0;
    private int sortdirection = -1;
    private MenuItem currentCheck;
    private ShareActionProvider myShareActionProvider;
    private Score selectedScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        databaseHelper = ScoreDataBase.getInstance(this);
        list = databaseHelper.getAllScores();

        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter = new ScoresAdapter(this, list);

        score = (ListView) findViewById(R.id.scoreListView);
        score.setAdapter(adapter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        score.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(parent.getContext(),view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_cell, popupMenu.getMenu());
                selectedScore = list.get(position);
                popupMenu.getMenu().findItem(R.id.delete_cell)
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item){
                                list.remove(selectedScore);
                                databaseHelper.deleteScore(selectedScore);
                                adapter.notifyDataSetChanged();
                                return true;
                            }
                        });

                popupMenu.getMenu().findItem(R.id.share_cell)
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                myShareActionProvider = (ShareActionProvider) item.getActionProvider();
                                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                intent.putExtra(Intent.EXTRA_SUBJECT, "Look at my score !!");
                                intent.putExtra(Intent.EXTRA_TEXT,"Look at my score !! \n"+
                                        selectedScore.getPoints() + " points on " +
                                                selectedScore.getNiveau() + " difficulty");
                                //startActivity(intent);
                                myShareActionProvider.setShareIntent(intent);
                                return true;
                            }
                        });
                popupMenu.show();
                return true;
            }

        });

    }

    public void removeAllScore(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Attention");
        alertDialog.setMessage("Do you want to delete all scores ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteAllScores();
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_score, menu);
        //MenuItem item = menu.findItem(R.id.share_cell);
        //myShareActionProvider = (ShareActionProvider) item.getActionProvider();
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
                if(currentCheck != null){
                    currentCheck.setChecked(false);
                }
                item.setChecked(true);
                currentCheck = item;
                sortby = 0;
                break;
            case R.id.sort_lvl :
                if(currentCheck != null){
                    currentCheck.setChecked(false);
                }
                item.setChecked(true);
                currentCheck = item;
                sortby = 1;
                break;
            case R.id.sort_time :
                if(currentCheck != null){
                    currentCheck.setChecked(false);
                }
                item.setChecked(true);
                currentCheck = item;
                sortby = 2;
                break;
            case R.id.delete_all:
                removeAllScore();
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
