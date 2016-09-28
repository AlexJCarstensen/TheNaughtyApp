package com.alexcarstensen.weatherornotyoulikeit.model;

/**
 * Created by Peter Ring on 27/09/2016.
 */

public class Main
{
    private float temp;
    private int humidity;
    private float pressure;
    private float temp_min;
    private float temp_max;

    public float getTemp()
    {
        return temp;
    }

    public float getTemp_min()
    {
        return temp_min;
    }

    public float getTemp_max()
    {
        return temp_max;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public float getPressure()
    {
        return pressure;
    }

    public void setTemp(float temp_)
    {
        temp = temp_;
    }

    public void setHumidity(int humidity_)
    {
        humidity = humidity_;
    }

    public void setPressure(float pressure_)
    {
        pressure = pressure_;
    }

    public void setTemp_min(float temp_min_)
    {
        temp_min = temp_min_;
    }

    public void setTemp_max(float temp_max_)
    {
        temp_max = temp_max_;
    }
}

