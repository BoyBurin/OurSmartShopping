package com.example.seniorproject.smartshopping.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ShoppingList;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;
import com.example.seniorproject.smartshopping.view.customviewgroup.ShoppingListView;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class ItemInventoryAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ItemInventory> itemInventories;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemInventoryAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        itemInventories = new ArrayList<ItemInventory>();
    }


    public void setItemInventories(ArrayList<ItemInventory> itemInventories){
        this.itemInventories = itemInventories;
    }


    @Override
    public int getCount() {

        return itemInventories.size();
    }

    @Override
    public Object getItem(int i) {

        return itemInventories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
            ItemView item;
            if (view != null)
                item = (ItemView) view;
            else
                item = new ItemView(viewGroup.getContext());

        ItemInventory itemInventory = (ItemInventory) getItem(position);
        item.setNameText(itemInventory.getName());
        item.setImageUrl(itemInventory.getPhotoUrl());
        item.setRemainder(itemInventory.getRemindItem().getSoft(), itemInventory.getRemindItem().getHard(),
                itemInventory.getAmount());
        lastPositionInteger.setValue(position);

            return item;

    }


}
