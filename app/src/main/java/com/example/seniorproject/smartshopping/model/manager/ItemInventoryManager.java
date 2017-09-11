package com.example.seniorproject.smartshopping.model.manager;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupMap;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;

import java.util.ArrayList;


public class ItemInventoryManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private static ItemInventoryManager instance;
    private ArrayList<ItemInventoryMap> itemInventoryMaps;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/


    public static ItemInventoryManager getInstance() {
        if (instance == null)
            instance = new ItemInventoryManager();
        return instance;
    }

    private Context mContext;

    private ItemInventoryManager() {

        mContext = Contextor.getInstance().getContext();
        itemInventoryMaps = new ArrayList<ItemInventoryMap>();
    }

    public ItemInventoryMap getItemInventory(int index) {

        return itemInventoryMaps.get(index);
    }


    public int getSize(){
        return itemInventoryMaps.size();
    }

    public void addItemInventory(ItemInventoryMap group){
        itemInventoryMaps.add(group);
    }


    public ArrayList<ItemInventory> getItemInventories(){
        ArrayList<ItemInventory> itemInventories = new ArrayList<ItemInventory>();
        for(ItemInventoryMap itemInventoryMap : this.itemInventoryMaps){
            itemInventories.add(itemInventoryMap.getItemInventory());
        }
        return itemInventories;
    }

}
