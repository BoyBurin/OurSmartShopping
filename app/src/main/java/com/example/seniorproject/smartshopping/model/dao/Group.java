package com.example.seniorproject.smartshopping.model.dao;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class Group implements Parcelable {

    private static Group instance;

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String name;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public Group(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/

    protected Group(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };



}
