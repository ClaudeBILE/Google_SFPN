package com.example.sfpn.google_sfpn;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ShareActionProvider;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;

public class Game extends AppCompatActivity implements OnMapReadyCallback, OnMapLongClickListener, OnStreetViewPanoramaReadyCallback {

    private SupportMapFragment map;
    private GoogleMap gMap;
    private SupportStreetViewPanoramaFragment streetView;
    private StreetViewPanorama mPanorama;
    private ArrayList<ArrayList<CustomPosition>> positionList = new ArrayList<ArrayList<CustomPosition>>() ;
    private ArrayList<CustomPosition>easyList = new ArrayList<CustomPosition>();
    private CustomPosition currentPosition;
    private final double circonferenceTerre = 40075/2.0;
    private Marker touchMarker;
    private Marker curentPositionMarker;
    private Polyline polyline;
    private Iterator<CustomPosition> itr;
    private int difficulty;
    private final int topScore = 10000;
    private Boolean isGameStarted = false;
    private double score;
    private int indiceList = 0;
    private Boolean isInvert = false;


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

            if (indiceList < positionList.get(difficulty).size()) {
                currentPosition = positionList.get(difficulty).get(indiceList);

                touchMarker = gMap.addMarker(new MarkerOptions()
                                .position(point)
                        //.title("Some title here")
                        //.snippet("Some description here")
                );

                curentPositionMarker = gMap.addMarker(new MarkerOptions()
                                .position(currentPosition.getPosition())
                        .title(currentPosition.getNom())
                        //.snippet("Some description here")
                );
                //trace poly
                PolylineOptions rectOptions = new PolylineOptions()
                        .add(currentPosition.getPosition()) // Same longitude, and 16km to the south
                        .add(point);
                polyline = gMap.addPolyline(rectOptions);

                float [] distance = new float[1];
                Location.distanceBetween(currentPosition.getPosition().latitude,currentPosition.getPosition().longitude,point.latitude,point.longitude,distance);
                Toast.makeText(getApplicationContext(), (indiceList+1)+" / "+ positionList.get(difficulty).size(), Toast.LENGTH_SHORT).show();
                if (isInvert == false) {
                    score = score + ((circonferenceTerre - distance[0] / 1000) / (circonferenceTerre)) * (topScore / positionList.get(difficulty).size());
                }
                else {

                    score = score + ((distance[0] / 1000) / (circonferenceTerre)) * (topScore / positionList.get(difficulty).size());
                }


                indiceList = indiceList + 1;
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                if (indiceList < positionList.get(difficulty).size()) {
                    mPanorama.setPosition(positionList.get(difficulty).get(indiceList).getPosition());
                    alertDialog.setMessage("Vous êtes à "+ distance[0]/1000 +"Km de "+ currentPosition.getNom());
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    touchMarker.remove();
                                    curentPositionMarker.remove();
                                    polyline.remove();
                                }
                            });
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                }
                else {
                    addScoreToDB((int) score, difficulty);
                    AlertDialog.Builder alertDialogbuild = new AlertDialog.Builder(this);
                    alertDialogbuild.setTitle("Bravo");
                    alertDialogbuild.setCancelable(false);
                    alertDialogbuild.setMessage("Votre score final est de "+ (int) score)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    touchMarker.remove();
                                    curentPositionMarker.remove();
                                    polyline.remove();
                                    goToMenu();
                                }
                            })
                            .setNegativeButton("Publish", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    touchMarker.remove();
                                    curentPositionMarker.remove();
                                    polyline.remove();
                                    goToMenu();
                                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    Score newScore = new Score((int)score, difficulty);

                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Look at my score !!");
                                    intent.putExtra(Intent.EXTRA_TEXT,"Look at my score !! \n"+
                                            newScore.getPoints() + " points on " +
                                            newScore.getNiveau() + " difficulty");
                                    startActivity(Intent.createChooser(intent,"Share via"));

                                }
                            }).show();


                    //save Score here and launch menu activity

                }
            }
        }

    }
    public  void goToMenu(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);

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
        isInvert = intent.getBooleanExtra("MODE", false);

        //eaysy mode
        easyList.add(new CustomPosition(new LatLng(48.8583698, 2.2944833000000244),"Tour Eiffel"));
        easyList.add(new CustomPosition(new LatLng(48.63601659999999, -1.5111144999999624),"Mont Saint Michel"));
        easyList.add(new CustomPosition(new LatLng(40.6892494, -74.0445004),"Statue de la liberté"));
        easyList.add(new CustomPosition(new LatLng(-33.8567844, 151.21529669999995),"Opéra de sydney"));
        easyList.add(new CustomPosition(new LatLng(51.50072919999999, -0.12462540000001354),"Big ben"));
        easyList.add(new CustomPosition(new LatLng(43.87910249999999, -103.4590667),"Mont rushmore"));
        easyList.add(new CustomPosition(new LatLng(43.722952, 10.396596999999929),"Tour de pise"));
        easyList.add(new CustomPosition(new LatLng(29.9772962, 31.132495500000005),"Pyramides de Gizeh"));
        easyList.add(new CustomPosition(new LatLng(27.1750151, 78.04215520000002),"Taj Mahal"));
        easyList.add(new CustomPosition(new LatLng(40.7485413, -73.98575770000002),"Empire state building"));
        //
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
        //Toast.makeText(getApplicationContext(), difficulty+"", Toast.LENGTH_SHORT).show();



    }

    public void Game(int difficulty){
            ArrayList<CustomPosition> levelPositionsList = positionList.get(difficulty);
            indiceList =0;
            score = 0;
            isGameStarted = true;

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

    public void addScoreToDB(int points, int difficulty){
        Score newScore = new Score(points,difficulty);
        ScoreDataBase dataBase = ScoreDataBase.getInstance(this);
        dataBase.addScore(newScore);
    }
}

