package com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class BasePurchaseItem {

    public static final int STORE_NAME = 0;
    public static final int TOTAL_PRICE = 1;
    public static final int SAVE_BUTTON = 2;
    public static final int PURCHASE_ITEM = 3;
    public static final int ADD_BUTTON = 4;

    private int type;

    public BasePurchaseItem(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }
}
