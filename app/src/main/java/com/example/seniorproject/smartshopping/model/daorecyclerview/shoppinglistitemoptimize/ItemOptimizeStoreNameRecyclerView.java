package com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ItemOptimizeStoreNameRecyclerView  extends BaseItemOptimize{

    private String storeName;

    public ItemOptimizeStoreNameRecyclerView() {
        super(BaseItemOptimize.STORE_NAME);
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
