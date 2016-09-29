package com.alexcarstensen.weatherornotyoulikeit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeppe on 23-09-2016.
 */


public class weatherItem  implements Parcelable{

    private int _id;
    private String _weatherStatus;
    private String _date;
    private String _temperature;
    private String _time;
    private String _intentAction;
    private int resultCode;

    public weatherItem(){};

    public weatherItem(String weatherStatus, String date, String temperature, String time, int weatherResultCode){

        this._weatherStatus = weatherStatus;
        this._date = date;
        this._temperature = temperature;
        this._time = time;
        this.resultCode = weatherResultCode;
    }

    protected weatherItem(Parcel in) {
        _id = in.readInt();
        _weatherStatus = in.readString();
        _date = in.readString();
        _temperature = in.readString();
        _time = in.readString();
        _intentAction = in.readString();
        resultCode = in.readInt();
    }

    public static final Creator<weatherItem> CREATOR = new Creator<weatherItem>() {
        @Override
        public weatherItem createFromParcel(Parcel in) {
            return new weatherItem(in);
        }

        @Override
        public weatherItem[] newArray(int size) {
            return new weatherItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(_id);
        dest.writeString(_weatherStatus);
        dest.writeString(_date);
        dest.writeString(_temperature);
        dest.writeString(_time);
        dest.writeString(_intentAction);
        dest.writeInt(resultCode);
    }
}