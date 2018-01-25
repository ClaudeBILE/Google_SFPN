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
    private ArrayList<CustomPosition>mediumList = new ArrayList<CustomPosition>();
    private ArrayList<CustomPosition>expertList = new ArrayList<CustomPosition>();
    private CustomPosition currentPosition;
    private final double circonferenceTerre = 40075/2.0;
    private Marker touchMarker;
    private Marker curentPositionMarker;
    private Polyline polyline;
    private int difficulty;
    private final int topScore = 10000;
    private Boolean isGameStarted = false;
    private double score;
    private int indiceList = 0;
    private Boolean isInvert = false;
    private Boolean isDialog1 = false;
    private Boolean isDialog2 = false;
    private String dialog2;
    private String dialog1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //if(isGameStarted == true) {
            outState.putInt("IndiceList", indiceList);
            outState.putBoolean("IsGameStart",isGameStarted);
            outState.putDouble("score",score);
            outState.putBoolean("isInvert",isInvert);
            outState.putBoolean("isDialog1",isDialog1);
            outState.putBoolean("isDialog2",isDialog2);
            outState.putString("dialog1",dialog1);
            outState.putString("dialog2",dialog2);
            outState.putInt("difficulty",difficulty);
            //outState.putInt("");
            super.onSaveInstanceState(outState);
        //}
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        indiceList = savedInstanceState.getInt("IndiceList");
        isGameStarted = savedInstanceState.getBoolean("IsGameStart");
        score = savedInstanceState.getDouble("score");
        isInvert = savedInstanceState.getBoolean("isInvert");
        difficulty = savedInstanceState.getInt("difficulty");
        isDialog1 = savedInstanceState.getBoolean("isDialog1");
        isDialog2 = savedInstanceState.getBoolean("isDialog2");
        dialog1 = savedInstanceState.getString("dialog1");
        dialog2 = savedInstanceState.getString("dialog2");
        if (isDialog1 == true){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage(dialog1);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (gMap != null) {
                                gMap.clear();
                            }
                            isDialog1 = false;
                        }
                    });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        if (isDialog2 == true){

            AlertDialog.Builder alertDialogbuild = new AlertDialog.Builder(this);
            alertDialogbuild.setTitle("Bravo");
            alertDialogbuild.setCancelable(false);

            alertDialogbuild.setMessage(dialog2)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (gMap != null) {
                                gMap.clear();
                            }

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
        }
    }

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
                    dialog1 = "Vous êtes à "+ distance[0]/1000 +"Km de "+ currentPosition.getNom();
                    alertDialog.setMessage(dialog1);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    touchMarker.remove();
                                    curentPositionMarker.remove();
                                    polyline.remove();
                                    isDialog1 = false;
                                }
                            });
                    alertDialog.setCanceledOnTouchOutside(false);
                    isDialog1 = true;
                    alertDialog.show();

                }
                else {
                    int inv_flag = isInvert?1:0;
                    isGameStarted = false;
                    addScoreToDB((int) score, difficulty*2 + inv_flag);

                    AlertDialog.Builder alertDialogbuild = new AlertDialog.Builder(this);
                    alertDialogbuild.setTitle("Bravo");
                    alertDialogbuild.setCancelable(false);
                    dialog2 = "Vous êtes à "+ distance[0]/1000 +"Km de "+ currentPosition.getNom() +
                            "\nVotre score final est de "+ (int) score;
                    alertDialogbuild.setMessage(dialog2)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isDialog2 = false;
                                    touchMarker.remove();
                                    curentPositionMarker.remove();
                                    polyline.remove();
                                    goToMenu();
                                }
                            })
                            .setNegativeButton("Publish", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isDialog2 = false;
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
                            });
                            //alertDialog.setCanceledOnTouchOutside(false);
                            isDialog2 = true;
                            alertDialogbuild.show();

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

        //easy mode
        easyList.add(new CustomPosition(new LatLng(48.8583698, 2.2944833000000244),"Tour Eiffel"));
        easyList.add(new CustomPosition(new LatLng(48.63601659999999, -1.5111144999999624),"Mont Saint Michel"));
        easyList.add(new CustomPosition(new LatLng(40.689684, -74.043467),"Statue de la liberté"));
        easyList.add(new CustomPosition(new LatLng(-33.8567844, 151.21529669999995),"Opéra de Sydney"));
        easyList.add(new CustomPosition(new LatLng(51.50072919999999, -0.12462540000001354),"Big Ben"));
        easyList.add(new CustomPosition(new LatLng(43.87910249999999, -103.4590667),"Mont Rushmore"));
        easyList.add(new CustomPosition(new LatLng(43.722952, 10.396596999999929),"Tour de Pise"));
        easyList.add(new CustomPosition(new LatLng(29.9772962, 31.132495500000005),"Pyramides de Gizeh"));
        easyList.add(new CustomPosition(new LatLng(27.1750151, 78.04215520000002),"Taj Mahal"));
        easyList.add(new CustomPosition(new LatLng(40.7485413, -73.98575770000002),"Empire state building"));
        // Medium mode
        mediumList.add(new CustomPosition(new LatLng(35.020950, 135.762192),"Kyōto-gosho"));
        mediumList.add(new CustomPosition(new LatLng(37.552278, 126.987358),"Seoul Tower"));
        mediumList.add(new CustomPosition(new LatLng(55.676618, 12.581232),"Christiansborgr"));
        mediumList.add(new CustomPosition(new LatLng(11.562584, 104.931531),"Palais royal de Phnom Penh"));
        mediumList.add(new CustomPosition(new LatLng( 45.498769, -73.570384 ),"Montréal"));
        mediumList.add(new CustomPosition(new LatLng( -34.608464, -58.373454),"Buenos Aires"));
        mediumList.add(new CustomPosition(new LatLng(23.140910, -82.351690),"Castillo de la Real Fuerza"));
        // Expert mode
        expertList.add(new CustomPosition(new LatLng(32.679867, -117.179628),"Corona Hotel, CA"));
        expertList.add(new CustomPosition(new LatLng(-8.433621, 115.278535),"Tegallalang Rice Terrace"));
        expertList.add(new CustomPosition(new LatLng(7.958417, 80.759725),"Sirigiya"));
        expertList.add(new CustomPosition(new LatLng(-33.437588, -70.651015),"Santiago Metropolitan Cathedral"));
        expertList.add(new CustomPosition(new LatLng(-43.459340, 171.179031),"Lake Heron"));
        expertList.add(new CustomPosition(new LatLng(41.305919, 69.295931),"Sacred Heart Catholic Cathedral, Ouzbekistan"));
        expertList.add(new CustomPosition(new LatLng( 64.749705, -23.889970 ),"Snæfellsjökull"));
        expertList.add(new CustomPosition(new LatLng(  36.610546, 27.837900  ),"Symi"));
        expertList.add(new CustomPosition(new LatLng( 6.813364, -5.295729 )," basilique de Yamoussoukro"));

        positionList.add(easyList);
        positionList.add(mediumList);
        positionList.add(expertList);
        Log.d("myTag", "This is my message "+difficulty);

        this.Game(difficulty);
        if (streetView == null){
            streetView = ((SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.street));
            if(streetView != null) {
                streetView.getStreetViewPanoramaAsync(this);
                Log.d("myTag", "streetview created and associated");
            }
        }


        this.setUpMapIfNeeded();
        if (savedInstanceState == null){
            map.setRetainInstance(true);
            streetView.setRetainInstance(true);

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
            if (mPanorama.getLocation() == null) {
                mPanorama.setPosition(levelPositionsList.get(0).getPosition());
            }
            Log.d("myTag", "Loading street view");
        }
    }

    public void addScoreToDB(int points, int difficulty){
        Score newScore = new Score(points,difficulty);
        ScoreDataBase dataBase = ScoreDataBase.getInstance(this);
        dataBase.addScore(newScore);
    }
}

