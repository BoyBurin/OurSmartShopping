package com.example.seniorproject.smartshopping.model.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boyburin on 9/8/2017 AD.
 */

public class ShoppingList implements Parcelable {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String name;
    private String descript;
    private String photoURL;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ShoppingList(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/

    protected ShoppingList(Parcel in) {
        name = in.readString();
        descript = in.readString();
        photoURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(descript);
        dest.writeString(photoURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };
}
