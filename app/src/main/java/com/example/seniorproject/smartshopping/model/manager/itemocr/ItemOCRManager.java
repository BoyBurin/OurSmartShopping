package com.example.seniorproject.smartshopping.model.manager.itemocr;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;

/**
 * Created by boyburin on 9/24/2017 AD.
 */

public class ItemOCRManager {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ItemOCR> itemOCRs;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemOCRManager() {
        itemOCRs = new ArrayList<ItemOCR>();
        mContext = Contextor.getInstance().getContext();
    }

    private Context mContext;

    public void addItemOCR(ItemOCR itemOCR){
        itemOCRs.add(itemOCR);
    }

    public ArrayList<ItemOCR> getItemOCRs(){
        return itemOCRs;
    }

    public int getSize(){
        return itemOCRs.size();
    }
}
