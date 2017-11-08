package com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory;

import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class ItemInventoryRecyclerView extends BaseItemInventory {

    ItemInventoryMap itemInventory;

    public ItemInventoryRecyclerView() {
        super(BaseItemInventory.ITEM_INVENTORY);
    }

    public ItemInventoryMap getItemInventory(){
        return itemInventory;
    }

    public void setItemInventory(ItemInventoryMap itemInventory){
        this.itemInventory = itemInventory;
    }
}
