package com.alexcarstensen.thebrandapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by jeppe on 03-10-2016.
 */

// REF: Made from ArniesFragmentsMovie example
@IgnoreExtraProperties
public class UserItem implements Parcelable{



    private String userName;
    private String email;
    private ArrayList<Contact> _contacts;

    public UserItem(){};

    public UserItem(String userName, String email){


        this.userName = userName;
        this.email = email;

        _contacts = new ArrayList<>();
    }

    protected UserItem(Parcel in) {

        userName = in.readString();
        email = in.readString();

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


    public void AddToList(Contact contact)
    {
        _contacts.add(contact);
    }

    public String get_userName() {return userName;}
    public void set_userName(String userName) {
        this.userName = userName;
    }

    public String get_email() {return email;}
    public void set_email(String email) {
        this.email = email;
    }


    public void set_password(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(userName);
        dest.writeString(email);

    }
}
