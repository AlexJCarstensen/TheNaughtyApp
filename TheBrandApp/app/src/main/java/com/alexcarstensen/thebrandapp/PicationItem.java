package com.alexcarstensen.thebrandapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeppe on 03-10-2016.
 */



// REF: Made from ArniesFragmentsMovie example
public class PicationItem implements Parcelable {


    private String _imageId;
    private String _pictureUrl;
    private String _latitude;
    private String _longtitude;
    private String _userId_fk;

    public PicationItem(){};

    public PicationItem(String imageId, String pictureUrl, String latitude, String longtitude, String userId_fk){

        this._imageId = imageId;
        this._pictureUrl = pictureUrl;
        this._latitude = latitude;
        this._longtitude = longtitude;
        this._userId_fk = userId_fk;
    }

    protected PicationItem(Parcel in) {
        _imageId = in.readString();
        _pictureUrl = in.readString();
        _latitude = in.readString();
        _longtitude = in.readString();
        _userId_fk = in.readString();
    }

    public static final Creator<PicationItem> CREATOR = new Creator<PicationItem>() {
        @Override
        public PicationItem createFromParcel(Parcel in) {
            return new PicationItem(in);
        }

        @Override
        public PicationItem[] newArray(int size) {
            return new PicationItem[size];
        }
    };


    public String get_imageId() {return _imageId;}
    public void set_imageId(String imageId) {this._imageId = imageId;}

    public String get_pictureUrl() {return _pictureUrl;}
    public void set_pictureUrl(String pictureUrl) {
        this._pictureUrl = pictureUrl;
    }

    public String get_latitude() {return _latitude;}
    public void set_latitude(String latitude) {
        this._latitude = latitude;
    }

    public String get_userId_fk() {return _userId_fk;}
    public void set_userId_fk(String latitude) {
        this._latitude = latitude;
    }

    public String get_longtitude() {return _longtitude;}
    public void set_longtitude(String longtitude) {
        this._longtitude = longtitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(_imageId);
        dest.writeString(_pictureUrl);
        dest.writeString(_latitude);
        dest.writeString(_longtitude);
        dest.writeString(_userId_fk);
    }
}