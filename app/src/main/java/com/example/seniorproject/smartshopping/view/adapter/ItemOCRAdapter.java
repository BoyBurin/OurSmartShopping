package com.example.seniorproject.smartshopping.view.adapter;

import android.content.ClipData;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.example.seniorproject.smartshopping.model.dao.UserMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupItemOCR;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupUser;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class ItemOCRAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ItemOCR> itemOCRs;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemOCRAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        itemOCRs = new ArrayList<ItemOCR>();
    }


    public void setItemOCRs(ArrayList<ItemOCR> itemOCRs){
        this.itemOCRs = itemOCRs;
    }


    @Override
    public int getCount() {

        return itemOCRs.size();
    }

    @Override
    public Object getItem(int i) {

        return itemOCRs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
            CustomViewGroupItemOCR item;
            if (view != null)
                item = (CustomViewGroupItemOCR) view;
            else
                item = new CustomViewGroupItemOCR(viewGroup.getContext());

        ItemOCR itemOCR = (ItemOCR) getItem(position);
        ItemInventory itemInventory = itemOCR.getItemInventoryMap().getItemInventory();
        double price = itemOCR.getPrice();
        long amount = itemOCR.getAmount();

        item.setName(itemInventory.getName());
        item.setAmount(amount);
        item.setPrice(price);
        item.setImage(itemInventory.getPhotoUrl());

        lastPositionInteger.setValue(position);

            return item;

    }


}
