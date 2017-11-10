package com.example.seniorproject.smartshopping.model.dao.itemocr;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;

/**
 * Created by boyburin on 9/24/2017 AD.
 */

public class ItemOCR implements Parcelable{

    private ItemInventoryMap itemInventoryMap;
    private double price;
    private long amount;

    public ItemOCR(){

    }

    public ItemOCR(ItemInventoryMap itemInventoryMap, double price, long amount){
        this.itemInventoryMap = itemInventoryMap;
        this.price = price;
        this.amount = amount;
    }

    protected ItemOCR(Parcel in) {
        itemInventoryMap = in.readParcelable(ItemInventoryMap.class.getClassLoader());
        price = in.readDouble();
        amount = in.readLong();
    }

    public ItemInventoryMap getItemInventoryMap() {
        return itemInventoryMap;
    }

    public void setItemInventoryMap(ItemInventoryMap itemInventoryMap) {
        this.itemInventoryMap = itemInventoryMap;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(itemInventoryMap, flags);
        dest.writeDouble(price);
        dest.writeLong(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemOCR> CREATOR = new Creator<ItemOCR>() {
        @Override
        public ItemOCR createFromParcel(Parcel in) {
            return new ItemOCR(in);
        }

        @Override
        public ItemOCR[] newArray(int size) {
            return new ItemOCR[size];
        }
    };
}
