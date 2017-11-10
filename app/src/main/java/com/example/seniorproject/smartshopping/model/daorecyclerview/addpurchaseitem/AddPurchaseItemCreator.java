package com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem;

import android.view.View;

import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class AddPurchaseItemCreator {

    public ArrayList<AddPurchaseItemRecyclerView> createAddPurchaseItem(ArrayList<ItemInventoryMap> itemInventories){
        ArrayList<AddPurchaseItemRecyclerView> addPurchaseItemRecyclerViews = new ArrayList<>();

        for(ItemInventoryMap itemInventory : itemInventories){
            AddPurchaseItemRecyclerView addPurchaseItemRecyclerView = new AddPurchaseItemRecyclerView();
            addPurchaseItemRecyclerView.setItemInventoryMap(itemInventory);

            addPurchaseItemRecyclerViews.add(addPurchaseItemRecyclerView);
        }

        return addPurchaseItemRecyclerViews;
    }

    public AddPurchaseItemButtonRecyclerView createAddPurchaseItemButton(View.OnClickListener cancelListener, View.OnClickListener addListener){
        AddPurchaseItemButtonRecyclerView addPurchaseItemButtonRecyclerView = new AddPurchaseItemButtonRecyclerView();

        addPurchaseItemButtonRecyclerView.setBtnCancel(cancelListener);
        addPurchaseItemButtonRecyclerView.setBtnAdd(addListener);

        return addPurchaseItemButtonRecyclerView;
    }
}
