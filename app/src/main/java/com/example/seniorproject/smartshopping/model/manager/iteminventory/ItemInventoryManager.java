package com.example.seniorproject.smartshopping.model.manager.iteminventory;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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

    public void removeItemInventory(int index){
        itemInventoryMaps.remove(index);
    }


    public int getSize(){
        return itemInventoryMaps.size();
    }

    public void addItemInventory(ItemInventoryMap group){
        itemInventoryMaps.add(group);
        Collections.sort(itemInventoryMaps, new ItemSort());
    }

    public void sortItem(){
        Collections.sort(itemInventoryMaps, new ItemSort());
    }


    public ArrayList<ItemInventory> getItemInventories(){
        ArrayList<ItemInventory> itemInventories = new ArrayList<ItemInventory>();
        for(ItemInventoryMap itemInventoryMap : this.itemInventoryMaps){
            itemInventories.add(itemInventoryMap.getItemInventory());
        }
        return itemInventories;
    }

    public ArrayList<ItemInventoryMap> getItemInventoryMaps(){
        return itemInventoryMaps;
    }


    public int getIndexByKey(String key){
        int index = -1;
        for(int i = 0 ; i < getSize() ; i++){
            if(key.equals(itemInventoryMaps.get(i).getId())){
                index = i;
                return index;
            }
        }
        return index;
    }

    public void reset(){
        itemInventoryMaps = new ArrayList<ItemInventoryMap>();
    }

    public boolean isContain(String barcodeId){
        for(ItemInventoryMap itemInventoryMap : itemInventoryMaps){
            if(itemInventoryMap.getItemInventory().getBarcodeId().equals(barcodeId)){
                return true;
            }
        }
        return false;
    }

    public class ItemSort implements Comparator<ItemInventoryMap> {

        @Override
        public int compare(ItemInventoryMap itemInventoryMap, ItemInventoryMap t1) {
            long value1 = itemInventoryMap.getItemInventory().getAmount() - itemInventoryMap.getItemInventory().getHard();
            long value2 = t1.getItemInventory().getAmount() - t1.getItemInventory().getHard();

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
