package com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem;

import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class PurchaseItemRecyclerView extends BasePurchaseItem {

    private PurchaseItemWithAction purchaseItemWithAction;

    public PurchaseItemRecyclerView() {
        super(BasePurchaseItem.PURCHASE_ITEM);
    }

    public PurchaseItemWithAction getPurchaseItemWithAction() {
        return purchaseItemWithAction;
    }

    public void setPurchaseItemWithAction(PurchaseItemWithAction purchaseItemWithAction) {
        this.purchaseItemWithAction = purchaseItemWithAction;
    }
}
