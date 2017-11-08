package com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class BaseItemInventory {

    public static final int TYPE_NAME = 0;
    public static final int ITEM_INVENTORY = 1;

    private int type;

    public BaseItemInventory(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }
}
