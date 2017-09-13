package com.example.seniorproject.smartshopping.model.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boyburin on 9/9/2017 AD.
 */

public class ShoppingListMap implements Parcelable{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String id;
    private ShoppingList shoppingList;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ShoppingListMap(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/


    protected ShoppingListMap(Parcel in) {
        id = in.readString();
        shoppingList = in.readParcelable(ShoppingList.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(shoppingList, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShoppingListMap> CREATOR = new Creator<ShoppingListMap>() {
        @Override
        public ShoppingListMap createFromParcel(Parcel in) {
            return new ShoppingListMap(in);
        }

        @Override
        public ShoppingListMap[] newArray(int size) {
            return new ShoppingListMap[size];
        }
    };
}
