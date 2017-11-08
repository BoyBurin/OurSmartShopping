package com.example.seniorproject.smartshopping.view.adapter.shoppinglist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingItemOptimize;

import java.util.ArrayList;

/**
 * Created by boyburin on 9/15/2017 AD.
 */

public class ItemOptimizeAdapter extends BaseAdapter {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ProductCrowd> productCrowds;

    private MutableInteger lastPositionInteger;

    //private ArrayList<View.OnClickListener> deleteListener;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemOptimizeAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        productCrowds = new ArrayList<ProductCrowd>();
    }


    public void setItemShoppingLists(ArrayList<ProductCrowd> productCrowds){
        this.productCrowds = productCrowds;
        //this.deleteListener = deleteListener;
    }


    @Override
    public int getCount() {

        return productCrowds.size();
    }

    @Override
    public Object getItem(int i) {

        return productCrowds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
        CustomViewGroupShoppingItemOptimize item;
        if (view != null)
            item = (CustomViewGroupShoppingItemOptimize) view;
        else
            item = new CustomViewGroupShoppingItemOptimize(viewGroup.getContext());

        ProductCrowd productCrowd = (ProductCrowd) getItem(position);
        item.setName(productCrowd.getName());
        item.setPrice(productCrowd.getPrice());
        item.setPlace(productCrowd.getStore());

        lastPositionInteger.setValue(position);

        return item;

    }

}
