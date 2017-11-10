package com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class BaseAddPurchaseItem {

    public static final int ADD_PURCHASE_ITEM = 0;
    public static final int ADD_PURCHASE_ITEM_BUTTON = 1;

    private int type;

    public BaseAddPurchaseItem(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }
}
