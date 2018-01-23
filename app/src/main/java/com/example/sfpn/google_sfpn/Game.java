package com.example.sfpn.google_sfpn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;

public class Game extends AppCompatActivity implements OnMapReadyCallback, OnMapLongClickListener, OnStreetViewPanoramaReadyCallback {

    private SupportMapFragment map;
    private GoogleMap gMap;
    private SupportStreetViewPanoramaFragment streetView;
    private StreetViewPanorama mPanorama;
    private ArrayList<ArrayList<CustomPosition>> positionList = new ArrayList<ArrayList<CustomPosition>>() ;
    private ArrayList<CustomPosition>easyList = new ArrayList<CustomPosition>();
    private static LatLng eiffel = new LatLng(48.8583698, 2.2944833000000244);
    private static LatLng saintMichel = new LatLng(48.636, -1.511);
    private Iterator<CustomPosition> itr;
    private int difficulty;
    private Boolean isGameStarted = false;


    protected void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

            // Check if we were successful in obtaining the map.
            if (map != null) {
                map.getMapAsync(this);
                Log.d("myTag", "map associated");
            }
        }
    }
    @Override
        public void onMapReady(GoogleMap map) {
        Log.d("myTag", "loading Map");
        loadMap(map);


    }

    @Override
    public void onMapLongClick(LatLng point) {
        if (isGameStarted == true) {
            Marker mapMarker = gMap.addMarker(new MarkerOptions()
                            .position(point)
                    //.title("Some title here")
                    //.snippet("Some description here")
            );
            Toast.makeText(getApplicationContext(), "Calcul de distance", Toast.LENGTH_SHORT).show();
            // Fake value to test the DB
            addScoreToDb((int)(Math.random()*1000), difficulty);
            mapMarker.remove();
        }

    }
    // The Map is verified. It is now safe to manipulate the map.
    protected void loadMap(GoogleMap googleMap) {
        if (googleMap != null) {
            // ... use map here
            gMap = googleMap;
            gMap.setOnMapLongClickListener(this);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("FLAG", 0);
        easyList.add(new CustomPosition(eiffel,"Tour Eiffel"));
        easyList.add(new CustomPosition(saintMichel,"Mont Saint Michel"));
        positionList.add(easyList);
        Log.d("myTag", "This is my message "+difficulty);
        this.Game(difficulty);
        if (streetView == null){
            streetView = ((SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.street));

        }

        this.setUpMapIfNeeded();

        if(streetView != null) {
            streetView.getStreetViewPanoramaAsync(this);
            Log.d("myTag", "streetview created and associated");
        }
        Log.d("myTag", "message 2");
        Toast.makeText(getApplicationContext(), difficulty+"", Toast.LENGTH_SHORT).show();
    }

    public void Game(int difficulty){
            ArrayList<CustomPosition> levelPositionsList = positionList.get(difficulty);
            itr = levelPositionsList.iterator();
            isGameStarted = true;
            if (itr.hasNext()) {
                CustomPosition currentPosition = itr.next();
            }
        Log.d("myTag", "launching Game");

    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        if (streetViewPanorama != null) {
            mPanorama = streetViewPanorama;

            ArrayList<CustomPosition> levelPositionsList = positionList.get(difficulty);
            mPanorama.setPosition(levelPositionsList.get(0).getPosition());

            Log.d("myTag", "Loading street view");
        }
    }

        //Toast.makeText(getApplicationContext(), message+"", Toast.LENGTH_SHORT).show();

    public void addScoreToDb(int points, int difficulty){
        Score newScore = new Score(points, difficulty);
        ScoreDataBase databaseHelper = ScoreDataBase.getInstance(this);

        // Add sample post to the database
        databaseHelper.addScore(newScore);
    }

}

