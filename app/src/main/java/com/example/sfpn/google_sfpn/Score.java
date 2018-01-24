package com.example.sfpn.google_sfpn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexis on 22/01/18.
 */

public class Score {
    private int points;
    private int niveau;
    private Date timestamp;
    private SimpleDateFormat ft =new SimpleDateFormat (" dd.MM.yyyy HH:mm");

    public Score(int d, int lvl){
        this.points = d;
        this.timestamp = new Date();
        this.niveau = lvl;

    }

    public Score(int d, int lvl, String date){
        this.points = d;
        this.niveau = lvl;
        try {
            this.timestamp = ft.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public String getPoints(){
        return (String.valueOf(points));
    }

    public int getPointsInt(){
        return points;
    }

    public String getNiveau(){
        switch (niveau){
            case 0:
                return "Novice";
            case 1:
                return "Inv Novice";
            case 2:
                return "Medium";
            case 3:
                return "Inv Novice";
            case 4:
                return "Expert";
            case 5:
                return "Inv Expert";
            default:
                return "";
        }
    }

    public int getNiveauInt(){
        return niveau;
    }

    public String getTimeStamp(){

        return ft.format(timestamp);
    }

    public Date getTimestampDate(){
        return timestamp;
    }

}
