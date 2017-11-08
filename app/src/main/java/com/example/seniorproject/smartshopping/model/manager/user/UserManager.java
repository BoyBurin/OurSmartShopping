package com.example.seniorproject.smartshopping.model.manager.user;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.user.UserMap;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class UserManager {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<UserMap> users;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public UserManager() {
        users = new ArrayList<UserMap>();
        mContext = Contextor.getInstance().getContext();
    }

    private Context mContext;

    public void addUser(UserMap user){
        users.add(user);
    }

    public ArrayList<UserMap> getUsers(){
        return users;
    }

}
