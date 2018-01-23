package com.example.sfpn.google_sfpn;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Henri-Philippe on 23/01/2018.
 */

public class CustomPosition {

    private LatLng position;
    private String nom;
    public CustomPosition(LatLng p,String s){
        position = p;
        nom = s;

    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
