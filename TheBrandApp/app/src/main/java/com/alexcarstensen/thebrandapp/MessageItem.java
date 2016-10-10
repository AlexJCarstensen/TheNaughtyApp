package com.alexcarstensen.thebrandapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeppe on 03-10-2016.
 */

// REF: Made from ArniesFragmentsMovie example
public class MessageItem implements Parcelable {

    private String _sender;
    private String _receiver;
    private String _message;
    private String _timestamp;
    private int _hasImage;
    private String _imageUrl;
    private Bitmap _imageBitmap;
    private String _latitude;
    private String _longitude;



    public MessageItem(){};

    public MessageItem( String sender, String _receiver, String message, String timestamp, int hasImage){
        this._sender = sender;
        this._receiver = _receiver;
        this._message = message;
        this._timestamp = timestamp;
        this._hasImage = hasImage;
    }

    protected MessageItem(Parcel in) {
        _sender = in.readString();
        _receiver = in.readString();
        _message = in.readString();
        _timestamp = in.readString();
        _hasImage = in.readInt();
        _imageUrl = in.readString();
        _imageBitmap = in.readParcelable(null); //REF: http://stackoverflow.com/questions/13417163/parcel-bitmap-android
        _latitude = in.readString();
        _longitude = in.readString();
    }

    public static final Creator<MessageItem> CREATOR = new Creator<MessageItem>() {
        @Override
        public MessageItem createFromParcel(Parcel in) {
            return new MessageItem(in);
        }

        @Override
        public MessageItem[] newArray(int size) {
            return new MessageItem[size];
        }
    };


    public String get_sender() {return _sender;}
    public void set_sender(String sender) {this._sender = sender;}

    public String get_receiver() {return _receiver;}
    public void set_receiver(String receiver) {
        this._receiver = receiver;
    }

    public String get_message() {return _message;}
    public void set_message(String message) {
        this._message = message;
    }

    public String get_timestamp() {return _timestamp;}
    public void set_timestamp(String timestamp) {
        this._timestamp = timestamp;
    }

    public String get_imageUrl() {return _imageUrl;}
    public void set_imageUrl(String timestamp) {
        this._timestamp = timestamp;
    }

    public int get_hasImage() {return _hasImage;}
    public void set_hasImage(int hasImage) {
        this._hasImage = hasImage;
    }

    public Bitmap get_imageBitmap() {return _imageBitmap;}
    public void set_imageBitmap(Bitmap imageBitmap) {
        this._imageBitmap = imageBitmap;
    }

    public String get_latitude() {return _latitude;}
    public void set_latitude(String latitude) {
        this._latitude = latitude;
    }

    public String get_longitude() {return _longitude;}
    public void set_longitude(String longtitude) {
        this._longitude = longtitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(_sender);
        dest.writeString(_receiver);
        dest.writeString(_message);
        dest.writeString(_timestamp);

        //Todo: check this.... and do something
        dest.writeInt(_hasImage);
        dest.writeString(_imageUrl);
        dest.writeParcelable(_imageBitmap,flags); // REF: http://stackoverflow.com/questions/13417163/parcel-bitmap-android
        dest.writeString(_latitude);
        dest.writeString(_longitude);
    }
}