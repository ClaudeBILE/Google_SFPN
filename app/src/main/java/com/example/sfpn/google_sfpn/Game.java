package com.example.sfpn.google_sfpn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.StreetViewPanorama;

public class Game extends AppCompatActivity {
    MapFragment map;
    MapFragment street;
    StreetViewPanorama streetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //streetView = (StreetViewPanorama) findViewById(R.id.street);
        Intent intent = getIntent();
        int message = intent.getIntExtra("FLAG", 0);


        //Toast.makeText(getApplicationContext(), message+"", Toast.LENGTH_SHORT).show();
    }


}
