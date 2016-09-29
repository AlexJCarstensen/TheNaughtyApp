package com.alexcarstensen.weatherornotyoulikeit;

/**
 * Created by jeppe on 23-09-2016.
 */


public class weatherItem {

    private int _id;
    private String _weatherStatus;
    private String _date;
    private String _temperature;
    private String _time;
    private String _intentAction;
    private int resultCode;


    public weatherItem(String weatherStatus, String date, String temperature, String time, int weatherResultCode){

        this._weatherStatus = weatherStatus;
        this._date = date;
        this._temperature = temperature;
        this._time = time;
        this.resultCode = weatherResultCode;
    }

    public int getID() {return _id;}
    public void setID(int id) {this._id = id;}

    public String getWeatherStatus() {return _weatherStatus;}
    public void setWeatherStatus(String weatherStatus) {
        this._weatherStatus = weatherStatus;
    }

    public String getDate() {return _date;}
    public void setDate(String date) {
        this._date = date;
    }

    public String getTemperature() {return _temperature;}
    public void setTemperature(String temperature) {
        this._temperature = temperature;
    }

    public String getTime() {return _time;}
    public void setTime(String time) {
        this._time = time;
    }

    public String getIntentAction() {
        return _intentAction;
    }
    public void setIntentAction(String intentAction) {
        this._intentAction = intentAction;
    }

    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}