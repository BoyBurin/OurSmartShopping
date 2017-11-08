package com.example.seniorproject.smartshopping.view.adapter.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.seniorproject.smartshopping.model.dao.user.User;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroup;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroupMap;
import com.example.seniorproject.smartshopping.model.dao.user.UserMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupUser;

import java.util.ArrayList;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class UserAdapter extends BaseAdapter{

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<UserInGroupMap> users;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public UserAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        users = new ArrayList<UserInGroupMap>();
    }


    public void setUsers(ArrayList<UserInGroupMap> users){
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

        UserInGroupMap userMap = (UserInGroupMap) getItem(position);
        UserInGroup user = userMap.getUser();
        item.setNameText(user.getName());
        item.setImageUrl(user.getPhotoUrl());
        lastPositionInteger.setValue(position);

            return item;

    }


}
