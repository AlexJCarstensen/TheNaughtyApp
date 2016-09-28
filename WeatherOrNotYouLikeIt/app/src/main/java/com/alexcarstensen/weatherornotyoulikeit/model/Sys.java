package com.alexcarstensen.weatherornotyoulikeit.model;

/**
 * Created by Peter Ring on 27/09/2016.
 */

public class Sys
{
    private String country;
    private int sunrise;
    private int sunset;

    public String getCountry()
    {
        return country;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }
}
