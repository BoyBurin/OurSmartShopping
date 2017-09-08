package com.example.seniorproject.smartshopping.model.manager;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class GroupManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private static GroupManager instance;
    private ArrayList<Group> groups = new ArrayList<Group>();
    private  Group currentGroup;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/


    public static GroupManager getInstance() {
        if (instance == null)
            instance = new GroupManager();
        return instance;
    }

    private Context mContext;

    private GroupManager() {

        mContext = Contextor.getInstance().getContext();
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group){
        groups.add(group);
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }
}
