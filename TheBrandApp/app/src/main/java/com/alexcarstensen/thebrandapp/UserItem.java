package com.alexcarstensen.thebrandapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeppe on 03-10-2016.
 */

public class UserItem implements Parcelable{


    private String _id;
    private String _userName;
    private String _email;
    private String _password;

    public UserItem(){};

    public UserItem(String id, String userName, String email, String password){

        this._id = id;
        this._userName = userName;
        this._email = email;
        this._password = password;
    }

    protected UserItem(Parcel in) {
        _id = in.readString();
        _userName = in.readString();
        _email = in.readString();
        _password = in.readString();
    }

    public static final Creator<UserItem> CREATOR = new Creator<UserItem>() {
        @Override
        public UserItem createFromParcel(Parcel in) {
            return new UserItem(in);
        }

        @Override
        public UserItem[] newArray(int size) {
            return new UserItem[size];
        }
    };


    public String get_id() {return _id;}
    public void set_id(String id) {this._id = id;}

    public String get_userName() {return _userName;}
    public void set_userName(String userName) {
        this._userName = userName;
    }

    public String get_email() {return _email;}
    public void set_email(String email) {
        this._email = email;
    }

    public String get_password() {return _password;}
    public void set_password(String email) {
        this._email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(_id);
        dest.writeString(_userName);
        dest.writeString(_email);
        dest.writeString(_password);
    }
}
