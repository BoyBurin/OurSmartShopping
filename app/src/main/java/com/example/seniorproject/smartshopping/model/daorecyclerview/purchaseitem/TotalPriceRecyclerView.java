package com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class TotalPriceRecyclerView extends BasePurchaseItem {

    private String totalPrice;

    public TotalPriceRecyclerView() {
        super(BasePurchaseItem.TOTAL_PRICE);
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
