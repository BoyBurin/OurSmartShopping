package com.example.seniorproject.smartshopping.model.manager.shoppinglist;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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
        Collections.sort(itemShoppingLists, new ItemSort());
    }

    public void sortItem(){
        Collections.sort(itemShoppingLists, new ItemSort());
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

    public ItemShoppingList getItemShoppingListByBarcode(String barcode){
        int index = getIndexByKey(barcode);

        return itemShoppingLists.get(index);
    }

    public class ItemSort implements Comparator<ItemShoppingList> {

        @Override
        public int compare(ItemShoppingList item1, ItemShoppingList item2) {
            long value1 = item1.getStatus();
            long value2 = item2.getStatus();

            if(value1 < value2){
                return -1;
            }
            else if(value1 > value2){
                return 1;
            }
            else{
                return 0;
            }
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }



}
