package com.alexcarstensen.weatherornotyoulikeit.model;

/**
 * Created by Peter Ring on 27/09/2016.
 */

public class Wind
{
    private double speed;
    private double deg;

    public double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
