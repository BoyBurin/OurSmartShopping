package com.example.seniorproject.smartshopping.model.dao;

import android.view.View;

/**
 * Created by boyburin on 9/12/2017 AD.
 */

public class ItemShoppingList {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private long amount;
    private String name;
    private String barcodeId;
    private View.OnClickListener deleteListener;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemShoppingList(){

    }

    public ItemShoppingList(long amount, String name, String barcodeId){
        this.amount = amount;
        this.name = name;
        this.barcodeId = barcodeId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    public void setDeleteListener(View.OnClickListener deleteListener){
        this.deleteListener = deleteListener;
    }

    public View.OnClickListener getDeleteListener(){
        return deleteListener;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/
}
