package com.example.seniorproject.smartshopping.model.dao;

import android.os.Parcel;
import android.os.Parcelable;


public class ItemInventory implements Parcelable {

    private static ItemInventory instance;

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String name;
    private long amount;
    private String comment;
    private String photoUrl;
    private RemindItem remindItem;
    private String unit;
    private String barcodeId;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemInventory(){

    }

    public ItemInventory(String name, long amount, String comment, String photoUrl, RemindItem remindItem, String unit, String barcodeId){

        this.name = name;
        this.amount = amount;
        this.comment = comment;
        this.photoUrl = photoUrl;
        this.remindItem = remindItem;
        this.unit = unit;
        this.barcodeId = barcodeId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public RemindItem getRemindItem() {
        return remindItem;
    }

    public void setRemindItem(RemindItem remindItem) {
        this.remindItem = remindItem;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/



    protected ItemInventory(Parcel in) {
        name = in.readString();
        amount = in.readLong();
        comment = in.readString();
        photoUrl = in.readString();
        remindItem = in.readParcelable(RemindItem.class.getClassLoader());
        unit = in.readString();
        barcodeId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(amount);
        dest.writeString(comment);
        dest.writeString(photoUrl);
        dest.writeParcelable(remindItem, flags);
        dest.writeString(unit);
        dest.writeString(barcodeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemInventory> CREATOR = new Creator<ItemInventory>() {
        @Override
        public ItemInventory createFromParcel(Parcel in) {
            return new ItemInventory(in);
        }

        @Override
        public ItemInventory[] newArray(int size) {
            return new ItemInventory[size];
        }
    };






}
