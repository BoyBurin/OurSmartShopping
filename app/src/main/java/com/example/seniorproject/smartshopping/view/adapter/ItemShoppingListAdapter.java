package com.example.seniorproject.smartshopping.view.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingListItem;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class ItemShoppingListAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ItemShoppingList> itemShoppingLists;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemShoppingListAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        itemShoppingLists = new ArrayList<ItemShoppingList>();
    }


    public void setItemShoppingLists(ArrayList<ItemShoppingList> itemShoppingLists){
        this.itemShoppingLists = itemShoppingLists;
    }


    @Override
    public int getCount() {

        return itemShoppingLists.size();
    }

    @Override
    public Object getItem(int i) {

        return itemShoppingLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
            CustomViewGroupShoppingListItem item;
            if (view != null)
                item = (CustomViewGroupShoppingListItem) view;
            else
                item = new CustomViewGroupShoppingListItem(viewGroup.getContext());

        ItemShoppingList itemShoppingList = (ItemShoppingList) getItem(position);
        long amount = itemShoppingList.getAmount();
        ItemInventory itemInventory = itemShoppingList.getItemInventoryMap()
                .getItemInventory();
        item.setItemName(itemInventory.getName());
        item.setAmount(amount);

        lastPositionInteger.setValue(position);

            return item;

    }


}
