package com.example.seniorproject.smartshopping.model.manager.shoppinglist;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class ItemShoppingListManager {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ItemShoppingList> itemShoppingLists;
    private Context mContext;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemShoppingListManager() {

        mContext = Contextor.getInstance().getContext();
        itemShoppingLists = new ArrayList<ItemShoppingList>();
    }

    public int getSize(){
        return itemShoppingLists.size();
    }

    public void addItemShoppingList(ItemShoppingList itemShoppingList){
        itemShoppingLists.add(itemShoppingList);
    }


    public ArrayList<ItemShoppingList> getItemShoppingLists(){
        return itemShoppingLists;
    }

    private int getIndexByKey(String key){
        int index = -1;
        for(int i = 0 ; i < getSize() ; i++){
            if(key.equals(itemShoppingLists.get(i).getBarcodeId())){
                index = i;
                return index;
            }
        }
        return index;
    }

    public void removeItemShoppingList(String barcode){
        int index = getIndexByKey(barcode);
        itemShoppingLists.remove(index);
    }



}
