package com.alexcarstensen.thebrandapp.model;

/**
 * Created by Peter Ring on 27/09/2016.
 */

public class Main
{
    private double temp;
    private int humidity;
    private double pressure;
    private double temp_min;
    private double temp_max;

    public double getTemp()
    {
        return temp;
    }

    public double getTemp_min()
    {
        return temp_min;
    }

    public double getTemp_max()
    {
        return temp_max;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public double getPressure()
    {
        return pressure;
    }

    public void setTemp(double temp_)
    {
        temp = temp_;
    }

    public void setHumidity(int humidity_)
    {
        humidity = humidity_;
    }

    public void setPressure(double pressure_)
    {
        pressure = pressure_;
    }

    public void setTemp_min(double temp_min_)
    {
        temp_min = temp_min_;
    }

    public void setTemp_max(double temp_max_)
    {
        temp_max = temp_max_;
    }
}

