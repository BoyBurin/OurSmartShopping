package com.example.seniorproject.smartshopping.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupList;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupGroupSetting;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class GroupSettingAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<GroupList> groups;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public GroupSettingAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        groups = new ArrayList<GroupList>();
    }


    public void setGroups(ArrayList<GroupList> groups){
        this.groups = groups;
    }


    @Override
    public int getCount() {

        return groups.size();
    }

    @Override
    public Object getItem(int i) {

        return groups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
            CustomViewGroupGroupSetting item;
            if (view != null)
                item = (CustomViewGroupGroupSetting) view;
            else
                item = new CustomViewGroupGroupSetting(viewGroup.getContext());

        GroupList group = (GroupList) getItem(position);
        item.setNameText(group.getName());
        item.setImageUrl(group.getPhotoUrl());
        lastPositionInteger.setValue(position);

            return item;

    }


}
