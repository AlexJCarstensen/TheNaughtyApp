package com.alexcarstensen.weatherornotyoulikeit.model;

/**
 * Created by Peter Ring on 27/09/2016.
 */

public class Wind
{
    private float speed;
    private float deg;

    public float getSpeed() {
        return speed;
    }

    public float getDeg() {
        return deg;
    }

    public void setDeg(float deg) {
        this.deg = deg;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
