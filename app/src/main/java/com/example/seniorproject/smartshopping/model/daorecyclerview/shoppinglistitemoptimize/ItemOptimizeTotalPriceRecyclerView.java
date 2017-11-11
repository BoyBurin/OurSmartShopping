package com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ItemOptimizeTotalPriceRecyclerView extends BaseItemOptimize{

    private double totalPrice;

    public ItemOptimizeTotalPriceRecyclerView() {
        super(BaseItemOptimize.TOTAL_PRICE);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
