package com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class BaseItemOptimize {

    public static final int STORE_NAME = 0;
    public static final int ITEM_OPTIMIZE = 1;
    public static final int TOTAL_PRICE = 2;;
    public static final int SAVE_PRICE = 3;;

    private int type;

    public BaseItemOptimize(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }
}
