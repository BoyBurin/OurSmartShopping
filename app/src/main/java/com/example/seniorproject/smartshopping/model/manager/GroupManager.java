package com.example.seniorproject.smartshopping.model.manager;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupList;
import com.example.seniorproject.smartshopping.model.dao.GroupMap;
import com.example.seniorproject.smartshopping.model.dao.ShoppingList;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.example.seniorproject.smartshopping.superuser.ProductList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GroupManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private static GroupManager instance;
    private ArrayList<GroupList> groups;
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
        groups = new ArrayList<GroupList>();
    }

    public GroupList getGroup(int index) {

        return groups.get(index);
    }


    public int getSize(){
        return groups.size();
    }

    public void addGroup(GroupList group){
        groups.add(group);
    }

    public GroupMap getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(GroupMap currentGroup) {
        this.currentGroup = currentGroup;
    }

    public ArrayList<GroupList> getGroups(){
        return groups;
    }

    public void reset(){
        groups = new ArrayList<GroupList>();
        currentGroup = null;
    }

    public boolean isContain(GroupList newGroup){
        for(GroupList groupList : groups){
            if(groupList.getId().equals(newGroup.getId())){
                return true;
            }
        }
        return false;
    }

}
