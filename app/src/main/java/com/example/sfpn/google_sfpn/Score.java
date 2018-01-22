package com.example.sfpn.google_sfpn;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexis on 22/01/18.
 */

public class Score {
    private Double points;
    private String niveau;
    private Date timestamp;
    private SimpleDateFormat ft =new SimpleDateFormat (" dd.MM.yyyy hh:mm");
    public Score(Double d, int lvl){
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

    public String getPoints(){
        return points.toString();
    }

    public String getNiveau(){
        return niveau;
    }

    public String getTimeStamp(){

        return ft.format(timestamp);
    }

}
