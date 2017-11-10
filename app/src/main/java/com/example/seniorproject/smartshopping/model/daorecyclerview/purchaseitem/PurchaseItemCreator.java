package com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem;

import android.view.View;

import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class PurchaseItemCreator {

    public StoreNameRecyclerView createStoreName(String storeName) {
        StoreNameRecyclerView storeNameRecyclerView = new StoreNameRecyclerView();
        storeNameRecyclerView.setStoreName(storeName);

        return storeNameRecyclerView;
    }

    public TotalPriceRecyclerView createTotalPrice(double totalPrice){
        TotalPriceRecyclerView totalPriceRecyclerView = new TotalPriceRecyclerView();
        totalPriceRecyclerView.setTotalPrice("" + totalPrice);

        return totalPriceRecyclerView;
    }

    public SaveButtonRecyclerView createSaveButton(View.OnClickListener saveListener){
        SaveButtonRecyclerView saveButtonRecyclerView = new SaveButtonRecyclerView();
        saveButtonRecyclerView.setSaveListener(saveListener);

        return saveButtonRecyclerView;
    }

    public ArrayList<PurchaseItemRecyclerView> createPurchaseItems(ArrayList<PurchaseItemWithAction> purchaseItemWithActions){
        ArrayList<PurchaseItemRecyclerView> newPurchaseItems = new ArrayList<>();

        for(PurchaseItemWithAction purchaseItemWithAction : purchaseItemWithActions){
            PurchaseItemRecyclerView purchaseItemRecyclerView = new PurchaseItemRecyclerView();
            purchaseItemRecyclerView.setPurchaseItemWithAction(purchaseItemWithAction);

            newPurchaseItems.add(purchaseItemRecyclerView);
        }

        return newPurchaseItems;
    }

    public AddButtonRecyclerView createAddButton(View.OnClickListener addListener){
        AddButtonRecyclerView addButtonRecyclerView = new AddButtonRecyclerView();
        addButtonRecyclerView.setAddListener(addListener);

        return addButtonRecyclerView;
    }
}
