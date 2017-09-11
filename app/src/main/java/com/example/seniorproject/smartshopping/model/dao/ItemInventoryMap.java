package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 9/9/2017 AD.
 */

public class ItemInventoryMap {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String id;
    private ItemInventory itemInventory;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemInventoryMap(){

    }

    public ItemInventoryMap(String id, ItemInventory itemInventory){
        this.id = id;
        this.itemInventory = itemInventory;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemInventory getItemInventory() {
        return itemInventory;
    }

    public void setItemInventory(ItemInventory itemInventory) {
        this.itemInventory = itemInventory;
    }
}
