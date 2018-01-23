package com.example.sfpn.google_sfpn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexis on 22/01/18.
 */

public class Score {
    private int points;
    private String niveau;
    private Date timestamp;
    private SimpleDateFormat ft =new SimpleDateFormat (" dd.MM.yyyy hh:mm");

    public Score(int d, int lvl){
        this.points = d;
        this.timestamp = new Date();

        if(lvl == 0){
            this.niveau = "Novice";
        } else if( lvl == 1){
            this.niveau = "Medium";
        } else if (lvl == 2){
            this.niveau ="Expert";
        }
    }

    public Score(int d, String lvl, String date){
        this.points = d;
        this.niveau = lvl;
        try {
            this.timestamp = ft.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public interface ScoreListener{
        public void onScoreReady();

        public void onDataLoaded(int score, int lvl);
    }

    public String getPoints(){
        return (String.valueOf(points));
    }

    public String getNiveau(){
        return niveau;
    }

    public String getTimeStamp(){

        return ft.format(timestamp);
    }

}
