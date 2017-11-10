package com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class StoreNameRecyclerView extends BasePurchaseItem {

    private String storeName;

    public StoreNameRecyclerView() {
        super(BasePurchaseItem.STORE_NAME);
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
