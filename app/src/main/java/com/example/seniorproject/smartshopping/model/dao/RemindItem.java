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
    private long soft;
    private long hard;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public RemindItem(){

    }


    public long getSoft() {
        return soft;
    }

    public void setSoft(long soft) {
        this.soft = soft;
    }

    public long getHard() {
        return hard;
    }

    public void setHard(long hard) {
        this.hard = hard;
    }

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/


    protected RemindItem(Parcel in) {
        soft = in.readLong();
        hard = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(soft);
        dest.writeLong(hard);
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
