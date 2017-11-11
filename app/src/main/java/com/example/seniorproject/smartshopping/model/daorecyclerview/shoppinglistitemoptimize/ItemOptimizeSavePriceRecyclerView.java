package com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ItemOptimizeSavePriceRecyclerView extends BaseItemOptimize{

    private long SavePrice;

    public ItemOptimizeSavePriceRecyclerView() {
        super(BaseItemOptimize.SAVE_PRICE);
    }

    public long getSavePrice() {
        return SavePrice;
    }

    public void setSavePrice(long savePrice) {
        SavePrice = savePrice;
    }
}
