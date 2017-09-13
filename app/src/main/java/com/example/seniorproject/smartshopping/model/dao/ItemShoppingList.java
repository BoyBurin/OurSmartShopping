package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 9/12/2017 AD.
 */

public class ItemShoppingList {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private long amount;
    private ItemInventoryMap itemInventoryMap;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemShoppingList(){

    }

    public ItemShoppingList(long amount, ItemInventoryMap itemInventoryMap){
        this.amount = amount;
        this.itemInventoryMap = itemInventoryMap;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public ItemInventoryMap getItemInventoryMap() {
        return itemInventoryMap;
    }

    public void setItemInventoryMap(ItemInventoryMap itemInventoryMap) {
        this.itemInventoryMap = itemInventoryMap;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/
}
