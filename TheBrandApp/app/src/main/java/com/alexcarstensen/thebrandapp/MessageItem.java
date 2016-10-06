package com.alexcarstensen.thebrandapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeppe on 03-10-2016.
 */


public class MessageItem implements Parcelable {


    private String _relation_fk;
    private String _sender;
    private String _receiver;
    private String _message;
    private String _timestamp;
    private String _hasImage;// Skal ændres til bool
    private String _imageId_fk;

    public MessageItem(){};

    public MessageItem(String relation_fk, String sender, String _receiver, String message, String timestamp, String hasImage, String imageId_fk){

        this._relation_fk = relation_fk;
        this._sender = sender;
        this._receiver = _receiver;
        this._message = message;
        this._timestamp = timestamp;
        this._hasImage = hasImage;
        this._imageId_fk = imageId_fk;
    }

    protected MessageItem(Parcel in) {
        _relation_fk = in.readString();
        _sender = in.readString();
        _receiver = in.readString();
        _message = in.readString();
        _timestamp = in.readString();
        _hasImage = in.readString();
        _imageId_fk = in.readString();
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


    public String get_relation_fk() {return _relation_fk;}
    public void set_relation_fk(String relation_fk) {this._relation_fk = relation_fk;}

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

    public String get_imageId_fk() {return _imageId_fk;}
    public void set_imageId_fk(String timestamp) {
        this._timestamp = timestamp;
    }

    public String get_hasImage() {return _hasImage;}
    public void set_hasImage(String hasImage) {
        this._hasImage = hasImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(_relation_fk);
        dest.writeString(_sender);
        dest.writeString(_receiver);
        dest.writeString(_message);
        dest.writeString(_timestamp);
        dest.writeString(_hasImage);
        dest.writeString(_imageId_fk);
    }
}