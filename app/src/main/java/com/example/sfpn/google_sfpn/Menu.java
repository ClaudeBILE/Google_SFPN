package com.example.sfpn.google_sfpn;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Menu extends AppCompatActivity implements View.OnClickListener{
    Button easy, medium, expert, statistics;
    boolean isInvert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Our buttons
        easy = (Button) findViewById(R.id.easy);
        medium = (Button) findViewById(R.id.medium);
        expert = (Button) findViewById(R.id.expert);
        statistics = (Button) findViewById(R.id.statistics);

        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        expert.setOnClickListener(this);
        statistics.setOnClickListener(this);

        // Invert Mode ?
        Switch switchButton = (Switch) findViewById(R.id.invert_mode);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isInvert = true;
                }else {
                    isInvert = false;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        // Start the game with sending the difficulty and the mode
        // or launch the Score Activity
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("MODE",isInvert);

        int id = view.getId();
        if(id == easy.getId()){
            intent.putExtra("FLAG", 0);
            startActivity(intent);
        }

        if (id == medium.getId()){
            intent.putExtra("FLAG", 2);
            startActivity(intent);
        }

        if (id == expert.getId()){
            intent.putExtra("FLAG", 4);
            startActivity(intent);
        }
        if (id == statistics.getId()){
            startActivity(new Intent(this, ScoreActivity.class));
        }
    }
}
