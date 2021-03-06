package com.alexcarstensen.weatherornotyoulikeit.helpers;

import java.security.Timestamp;
import java.sql.Time;

/**
 * Created by ajc on 22-09-2016.
 */

public class WeatherDAO {
    private int _id;
    private String _description;
    private float _temperature;
    private String _timestamp;

    public WeatherDAO(){}

    public WeatherDAO(String description, float temperature, String timestamp){
        this._description = description;
        this._temperature = temperature;
        this._timestamp = timestamp;
    }

    /*Get and SET ID*/
    public int getID(){
        return this._id;
    }
    public void setID(int ID){ this._id = ID; }

    /*Get and set description*/
    public void setDescription(String description){
        this._description = description;
    }

    public String getDescription(){
        return this._description;
    }

    /*Get and set temperature*/
    public void setTemperature(float temperature){
        this._temperature = temperature;
    }

    public float getTemperature(){
        return this._temperature;
    }

    /*Get and set Timestamp*/
    public void setTimestamp(String timestamp){
        this._timestamp = timestamp;
    }

    public String getTimestamp(){
        return this._timestamp;
    }

}
