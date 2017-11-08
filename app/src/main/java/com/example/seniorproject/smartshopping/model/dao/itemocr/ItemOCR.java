package com.example.seniorproject.smartshopping.model.dao.itemocr;

import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;

/**
 * Created by boyburin on 9/24/2017 AD.
 */

public class ItemOCR {

    private ItemInventoryMap itemInventoryMap;
    private double price;
    private long amount;

    public ItemOCR(){

    }

    public ItemOCR(ItemInventoryMap itemInventoryMap, double price, long amount){
        this.itemInventoryMap = itemInventoryMap;
        this.price = price;
        this.amount = amount;
    }

    public ItemInventoryMap getItemInventoryMap() {
        return itemInventoryMap;
    }

    public void setItemInventoryMap(ItemInventoryMap itemInventoryMap) {
        this.itemInventoryMap = itemInventoryMap;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
