package com.example.seniorproject.smartshopping.model.manager.shoppinglist;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingList;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;


public class ShoppingListManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ShoppingListMap> shoppingListMaps;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/



    private Context mContext;

    public ShoppingListManager() {

        mContext = Contextor.getInstance().getContext();
        shoppingListMaps = new ArrayList<ShoppingListMap>();
    }

    public int getSize(){
        return shoppingListMaps.size();
    }

    public ShoppingListMap getShoppingList(int index) {
        return shoppingListMaps.get(index);
    }


    public void addShoppingList(ShoppingListMap shoppingList){
        shoppingListMaps.add(shoppingList);
    }


    public void insertDaoAtTopPosition(ShoppingListMap shoppingList){

        shoppingListMaps.add(0, shoppingList);
    }

    public ArrayList<ShoppingList> getShoppingLists(){
        ArrayList<ShoppingList> shoppingLists = new ArrayList<ShoppingList>();
        for(ShoppingListMap shoppingListMap : shoppingListMaps){
            shoppingLists.add(shoppingListMap.getShoppingList());
        }

        return shoppingLists;
    }

    public void reset(){
        shoppingListMaps = new ArrayList<ShoppingListMap>();
    }

    private int getIndexByKey(String key){
        int index = -1;
        for(int i = 0 ; i < getSize() ; i++){
            if(key.equals(shoppingListMaps.get(i).getId())){
                index = i;
                return index;
            }
        }
        return index;
    }

    public void deleteShoppingList(String key){
        int index = getIndexByKey(key);
        shoppingListMaps.remove(index);
    }


}
