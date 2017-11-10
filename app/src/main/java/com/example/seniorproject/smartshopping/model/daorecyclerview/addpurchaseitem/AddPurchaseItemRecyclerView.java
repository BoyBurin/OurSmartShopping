package com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem;

import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class AddPurchaseItemRecyclerView extends BaseAddPurchaseItem {

    private ItemInventoryMap itemInventoryMap;

    public AddPurchaseItemRecyclerView() {
        super(BaseAddPurchaseItem.ADD_PURCHASE_ITEM);
    }

    public ItemInventoryMap getItemInventoryMap() {
        return itemInventoryMap;
    }

    public void setItemInventoryMap(ItemInventoryMap itemInventoryMap) {
        this.itemInventoryMap = itemInventoryMap;
    }
}
