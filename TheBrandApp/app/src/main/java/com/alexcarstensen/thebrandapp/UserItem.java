package com.alexcarstensen.thebrandapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

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
    private List<Contact> _contacts;

    public UserItem(){};

    public UserItem(String userName, String email){


        this.userName = userName;
        this.email = email;

        _contacts = new List<Contact>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Contact> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Contact contact) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Contact> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends Contact> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Contact get(int index) {
                return null;
            }

            @Override
            public Contact set(int index, Contact element) {
                return null;
            }

            @Override
            public void add(int index, Contact element) {

            }

            @Override
            public Contact remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Contact> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Contact> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<Contact> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
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
