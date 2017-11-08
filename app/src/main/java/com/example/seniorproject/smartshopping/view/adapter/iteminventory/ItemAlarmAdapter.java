package com.example.seniorproject.smartshopping.view.adapter.iteminventory;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.alarm.ItemAlarm;
import com.example.seniorproject.smartshopping.model.dao.alarm.ItemAlarmIdAction;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupAlarmItem;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class ItemAlarmAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ItemAlarmIdAction> itemAlarmIdActions;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemAlarmAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        itemAlarmIdActions = new ArrayList<ItemAlarmIdAction>();
    }


    public void setGroups(ArrayList<ItemAlarmIdAction> itemAlarmIdActions){
        this.itemAlarmIdActions = itemAlarmIdActions;
    }


    @Override
    public int getCount() {

        return itemAlarmIdActions.size();
    }

    @Override
    public Object getItem(int i) {

        return itemAlarmIdActions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
            CustomViewGroupAlarmItem item;
            if (view != null)
                item = (CustomViewGroupAlarmItem) view;
            else
                item = new CustomViewGroupAlarmItem(viewGroup.getContext());

        ItemAlarmIdAction itemAlarmIdAction = (ItemAlarmIdAction) getItem(position);
        ItemAlarm itemAlarm = itemAlarmIdAction.getItemAlarm();

        item.setDay(itemAlarm.getDay());
        item.setTime(itemAlarm.getHour(), itemAlarm.getMinute());
        item.setDeleteListener(itemAlarmIdAction.getDeleteListener());

        lastPositionInteger.setValue(position);

            return item;

    }


}
