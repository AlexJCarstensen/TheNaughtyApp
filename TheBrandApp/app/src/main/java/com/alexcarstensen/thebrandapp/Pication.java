package com.alexcarstensen.thebrandapp;

import android.net.Uri;

/**
 * Created by Peter Ring on 11/10/2016.
 */

public class Pication {

    private String Url;
    private Double Lat;
    private Double Lon;

    public Pication(){}

    public Pication(String url, Double lat, Double lon)
    {
        Url = url;
        Lat = lat;
        Lon = lon;
    }


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        this.Lat = lat;
    }

    public Double getLon() {
        return Lon;
    }

    public void setLon(Double lon) {
        this.Lon = lon;
    }
}
