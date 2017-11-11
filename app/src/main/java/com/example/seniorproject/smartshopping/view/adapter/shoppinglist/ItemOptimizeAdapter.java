package com.example.seniorproject.smartshopping.view.adapter.shoppinglist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingListItemOptimize;

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
        CustomViewGroupShoppingListItemOptimize item;
        if (view != null)
            item = (CustomViewGroupShoppingListItemOptimize) view;
        else
            item = new CustomViewGroupShoppingListItemOptimize(viewGroup.getContext());

        ProductCrowd productCrowd = (ProductCrowd) getItem(position);
        item.setName(productCrowd.getName());
        item.setPrice(productCrowd.getPrice());

        lastPositionInteger.setValue(position);

        return item;

    }

}
