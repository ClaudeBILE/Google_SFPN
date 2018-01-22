package com.example.sfpn.google_sfpn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Menu extends AppCompatActivity implements View.OnClickListener{
    Button easy, medium, expert, statistics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        easy = (Button) findViewById(R.id.easy);
        medium = (Button) findViewById(R.id.medium);
        expert = (Button) findViewById(R.id.expert);
        statistics = (Button) findViewById(R.id.statistics);
        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        expert.setOnClickListener(this);
        statistics.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, Game.class);
        int id = view.getId();
        if(id == easy.getId()){
            intent.putExtra("FLAG", 0);
            startActivity(intent);
        }

        if (id == medium.getId()){
            intent.putExtra("FLAG", 1);
            startActivity(intent);
        }

        if (id == expert.getId()){
            intent.putExtra("FLAG", 2);
            startActivity(intent);
        }
        if (id == statistics.getId()){
            startActivity(new Intent(this, ScoreActivity.class));
        }
    }
}
