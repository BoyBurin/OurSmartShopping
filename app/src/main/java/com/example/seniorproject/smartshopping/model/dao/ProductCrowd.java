package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 9/15/2017 AD.
 */

public class ProductCrowd {
    private String barcode;
    private String name;
    private long price;
    private String store;


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
