package com.example.seniorproject.smartshopping.model.dao;

import android.os.Parcel;
import android.os.Parcelable;


public class ItemInventory implements Parcelable {

    private static ItemInventory instance;

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String name;
    private int amount;
    private String comment;
    private String photoUrl;
    private RemindItem remindItem;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemInventory(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/

    protected ItemInventory(Parcel in) {
        name = in.readString();
        amount = in.readInt();
        comment = in.readString();
        photoUrl = in.readString();
        remindItem = in.readParcelable(RemindItem.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(amount);
        dest.writeString(comment);
        dest.writeString(photoUrl);
        dest.writeParcelable(remindItem, flags);
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
