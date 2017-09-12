package com.example.seniorproject.smartshopping.model.manager;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupMap;
import com.example.seniorproject.smartshopping.model.dao.ShoppingList;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GroupManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private static GroupManager instance;
    private ArrayList<GroupMap> groups;
    private GroupMap currentGroup;

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
        groups = new ArrayList<GroupMap>();
    }

    public GroupMap getGroup(int index) {

        return groups.get(index);
    }


    public int getSize(){
        return groups.size();
    }

    public void addGroup(GroupMap group){
        groups.add(group);
    }

    public GroupMap getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(GroupMap currentGroup) {
        this.currentGroup = currentGroup;
    }

    public ArrayList<Group> getGroups(){
        ArrayList<Group> groups = new ArrayList<Group>();
        for(GroupMap groupMap : this.groups){
            groups.add(groupMap.getGroup());
        }
        return groups;
    }

    public void reset(){
        groups = new ArrayList<GroupMap>();
        currentGroup = null;
    }

}
