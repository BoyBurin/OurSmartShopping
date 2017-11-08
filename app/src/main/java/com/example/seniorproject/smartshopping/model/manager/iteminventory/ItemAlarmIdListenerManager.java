package com.example.seniorproject.smartshopping.model.manager.iteminventory;

import android.content.Context;


import com.example.seniorproject.smartshopping.model.dao.alarm.ItemAlarmIdAction;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;



public class ItemAlarmIdListenerManager {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ItemAlarmIdAction> itemList;
    private Context mContext;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemAlarmIdListenerManager() {

        mContext = Contextor.getInstance().getContext();
        itemList = new ArrayList<ItemAlarmIdAction>();
    }

    public int getSize(){
        return itemList.size();
    }

    public void addItemAlarm(ItemAlarmIdAction itemAlarmIdAction){
        itemList.add(itemAlarmIdAction);
    }


    public ArrayList<ItemAlarmIdAction> getItemAlarms(){
        return itemList;
    }

    private int getIndexByKey(String key){
        int index = -1;
        for(int i = 0 ; i < getSize() ; i++){
            if(key.equals(itemList.get(i).getId())){
                index = i;
                return index;
            }
        }
        return index;
    }

    public void removeItemAlarm(String id){
        int index = getIndexByKey(id);
        itemList.remove(index);
    }



}
