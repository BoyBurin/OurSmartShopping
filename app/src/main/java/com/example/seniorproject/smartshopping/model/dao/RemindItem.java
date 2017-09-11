package com.example.seniorproject.smartshopping.model.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boyburin on 9/11/2017 AD.
 */

public class RemindItem implements Parcelable{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/
    private int soft;
    private int hard;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public RemindItem(){

    }


    public int getSoft() {
        return soft;
    }

    public void setSoft(int soft) {
        this.soft = soft;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/


    protected RemindItem(Parcel in) {
        soft = in.readInt();
        hard = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(soft);
        dest.writeInt(hard);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RemindItem> CREATOR = new Creator<RemindItem>() {
        @Override
        public RemindItem createFromParcel(Parcel in) {
            return new RemindItem(in);
        }

        @Override
        public RemindItem[] newArray(int size) {
            return new RemindItem[size];
        }
    };
}
