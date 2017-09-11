package com.example.seniorproject.smartshopping.model.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boyburin on 9/9/2017 AD.
 */

public class ItemInventoryMap implements Parcelable{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String id;
    private ItemInventory itemInventory;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemInventoryMap(){

    }

    public ItemInventoryMap(String id, ItemInventory itemInventory){
        this.id = id;
        this.itemInventory = itemInventory;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemInventory getItemInventory() {
        return itemInventory;
    }

    public void setItemInventory(ItemInventory itemInventory) {
        this.itemInventory = itemInventory;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/

    protected ItemInventoryMap(Parcel in) {
        id = in.readString();
        itemInventory = in.readParcelable(ItemInventory.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(itemInventory, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemInventoryMap> CREATOR = new Creator<ItemInventoryMap>() {
        @Override
        public ItemInventoryMap createFromParcel(Parcel in) {
            return new ItemInventoryMap(in);
        }

        @Override
        public ItemInventoryMap[] newArray(int size) {
            return new ItemInventoryMap[size];
        }
    };
}
