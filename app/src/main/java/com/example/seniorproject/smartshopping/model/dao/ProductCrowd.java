package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 9/15/2017 AD.
 */

public class ProductCrowd {
    private String name;
    private double price;
    private String store;

    public ProductCrowd(){

    }

    public ProductCrowd(String name, double price, String store){
        this.name = name;
        this.price = price;
        this.store = store;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
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
