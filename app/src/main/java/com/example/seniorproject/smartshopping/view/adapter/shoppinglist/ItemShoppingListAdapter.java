package com.example.seniorproject.smartshopping.view.adapter.shoppinglist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingListItem;

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

    //private ArrayList<View.OnClickListener> deleteListener;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemShoppingListAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        itemShoppingLists = new ArrayList<ItemShoppingList>();
    }


    public void setItemShoppingLists(ArrayList<ItemShoppingList> itemShoppingLists){
        this.itemShoppingLists = itemShoppingLists;
        //this.deleteListener = deleteListener;
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
        item.setItemName(itemShoppingList.getName());
        item.setAmount(amount);
        item.setDeleteListener(itemShoppingLists.get(position).getDeleteListener());

        lastPositionInteger.setValue(position);

            return item;

    }


}
