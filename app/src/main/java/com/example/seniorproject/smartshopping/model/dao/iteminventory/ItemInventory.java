package com.example.seniorproject.smartshopping.model.dao.iteminventory;

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
    private long hard;
    private long soft;
    private String unit;
    private String barcodeId;
    private String type;
    private double retailPrice;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemInventory(){

    }

    public ItemInventory(String name, long amount, String comment, String photoUrl, String unit, String barcodeId
    , long hard, long soft, String type, double retailPrice){

        this.name = name;
        this.amount = amount;
        this.comment = comment;
        this.photoUrl = photoUrl;
        this.unit = unit;
        this.barcodeId = barcodeId;
        this.hard = hard;
        this.soft = soft;
        this.type = type;
        this.retailPrice = retailPrice;
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

    public long getHard() {
        return hard;
    }

    public void setHard(long hard) {
        this.hard = hard;
    }

    public long getSoft() {
        return soft;
    }

    public void setSoft(long soft) {
        this.soft = soft;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/



    protected ItemInventory(Parcel in) {
        name = in.readString();
        amount = in.readLong();
        comment = in.readString();
        photoUrl = in.readString();
        unit = in.readString();
        barcodeId = in.readString();
        hard = in.readLong();
        soft = in.readLong();
        type = in.readString();
        retailPrice = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(amount);
        dest.writeString(comment);
        dest.writeString(photoUrl);
        dest.writeString(unit);
        dest.writeString(barcodeId);
        dest.writeLong(hard);
        dest.writeLong(soft);
        dest.writeString(type);
        dest.writeDouble(retailPrice);
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
