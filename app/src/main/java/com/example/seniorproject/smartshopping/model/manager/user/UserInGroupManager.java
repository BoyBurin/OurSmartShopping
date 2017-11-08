package com.example.seniorproject.smartshopping.model.manager.user;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.user.UserInGroup;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroupMap;
import com.example.seniorproject.smartshopping.model.dao.user.UserMap;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class UserInGroupManager {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<UserInGroupMap> users;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public UserInGroupManager() {
        users = new ArrayList<UserInGroupMap>();
        mContext = Contextor.getInstance().getContext();
    }

    private Context mContext;

    public void addUser(UserInGroupMap user){
        users.add(user);
        Collections.sort(users, new ItemSort());
    }

    public ArrayList<UserInGroupMap> getUsers(){
        return users;
    }

    public void sortItem(){
        Collections.sort(users, new ItemSort());
    }

    public class ItemSort implements Comparator<UserInGroupMap> {

        @Override
        public int compare(UserInGroupMap user1, UserInGroupMap user2) {
            return user1.getUser().getName().compareTo(user2.getUser().getName());

        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }

}
