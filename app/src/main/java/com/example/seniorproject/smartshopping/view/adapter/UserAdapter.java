package com.example.seniorproject.smartshopping.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.example.seniorproject.smartshopping.model.dao.UserMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupUser;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class UserAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<UserMap> users;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public UserAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        users = new ArrayList<UserMap>();
    }


    public void setUsers(ArrayList<UserMap> users){
        this.users = users;
    }


    @Override
    public int getCount() {

        return users.size();
    }

    @Override
    public Object getItem(int i) {

        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
            CustomViewGroupUser item;
            if (view != null)
                item = (CustomViewGroupUser) view;
            else
                item = new CustomViewGroupUser(viewGroup.getContext());

        UserMap userMap = (UserMap) getItem(position);
        User user = userMap.getUser();
        item.setNameText(user.getName());
        item.setImageUrl(user.getPhotoUrl());
        lastPositionInteger.setValue(position);

            return item;

    }


}
