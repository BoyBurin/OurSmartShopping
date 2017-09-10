package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 9/9/2017 AD.
 */

public class ShoppingListMap {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String id;
    private ShoppingList shoppingList;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ShoppingListMap(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }
}
