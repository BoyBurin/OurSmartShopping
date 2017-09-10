package com.example.seniorproject.smartshopping.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ShoppingList;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.ShoppingListView;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class ShoppingListAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ShoppingList> shoppingLists;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ShoppingListAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        shoppingLists = new ArrayList<ShoppingList>();
    }


    public void setShoppingLists(ArrayList<ShoppingList> shoppingLists){
        this.shoppingLists = shoppingLists;
    }


    @Override
    public int getCount() {

        return shoppingLists.size();
    }

    @Override
    public Object getItem(int i) {

        return shoppingLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
            ShoppingListView item;
            if (view != null)
                item = (ShoppingListView) view;
            else
                item = new ShoppingListView(viewGroup.getContext());

        ShoppingList shoppingList = (ShoppingList) getItem(position);
        item.setNameText(shoppingList.getName());
        item.setDescriptionText(shoppingList.getDescript());
        item.setImageUrl(shoppingList.getPhotoURL());


        if(position> lastPositionInteger.getValue()) {
            Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                    R.anim.up_from_bottom);

            item.startAnimation(anim);
            lastPositionInteger.setValue(position);
        }

            return item;

    }


}
