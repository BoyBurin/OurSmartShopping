package com.example.seniorproject.smartshopping.model.manager;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemShoppingList;

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

    public int getIndexByKey(String key){
        int index = -1;
        for(int i = 0 ; i < getSize() ; i++){
            if(key.equals(itemShoppingLists.get(i).getItemInventoryMap().getId())){
                index = i;
                return index;
            }
        }
        return index;
    }



}
