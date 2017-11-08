package com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory;

import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class ItemInventoryCreator {

    public  ArrayList<BaseItemInventory> createItemInventories(ArrayList<ItemInventoryMap> itemInventories, ArrayList<String> types){

        Collections.sort(itemInventories, new ItemSort());

        ArrayList<BaseItemInventory> newItemInventories = new ArrayList<>();
        for(String type : types){
            ItemInventoryTypeNameRecyclerView itemTypeName = new ItemInventoryTypeNameRecyclerView();
            itemTypeName.setTypeName(type);
            newItemInventories.add(itemTypeName);

            for(ItemInventoryMap item : itemInventories){

                if(item.getItemInventory().getType().equals(type)) {
                    ItemInventoryRecyclerView itemInventory = new ItemInventoryRecyclerView();
                    itemInventory.setItemInventory(item);
                    newItemInventories.add(itemInventory);
                }
            }
        }

        return newItemInventories;

    }

    public ArrayList<String> getTypes(ArrayList<ItemInventoryMap> itemInventories){
        ArrayList<String> types = new ArrayList<>();

        for(ItemInventoryMap item : itemInventories){
            String itemType = item.getItemInventory().getType();
            if(!types.contains(itemType)){
                types.add(itemType);
            }
        }
        return types;
    }

    public ArrayList<String> updateTypes(ArrayList<String> types, ArrayList<ItemInventoryMap> itemInventories){
        boolean exist = false;
        for(int i = 0 ; i < types.size() ; i++){
            for(ItemInventoryMap item : itemInventories){

                if(item.getItemInventory().getType().equals(types.get(i))) {
                    exist = true;
                    break;
                }
            }

            if(!exist){
                types.remove(i);
            }
            else{
                exist = false;
            }
        }

        ArrayList<String> newTypes = getTypes(itemInventories);

        for(String type : newTypes){
            if(!types.contains(type)){
                types.add(type);
            }
        }

        return types;
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
