package com.example.seniorproject.smartshopping.model.manager.group;

import android.content.Context;


import com.example.seniorproject.smartshopping.model.dao.group.GroupWating;
import com.example.seniorproject.smartshopping.model.dao.group.GroupWatingListener;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;


public class GroupWatingManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<GroupWatingListener> groups;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/



    private Context mContext;

    public GroupWatingManager() {

        mContext = Contextor.getInstance().getContext();
        groups = new ArrayList<GroupWatingListener>();
    }

    public GroupWatingListener getGroup(int index) {

        return groups.get(index);
    }


    public int getSize(){
        return groups.size();
    }

    public void addGroup(GroupWatingListener group){
        groups.add(group);
    }



    public ArrayList<GroupWatingListener> getGroups(){
        return groups;
    }


    public boolean isContain(GroupWatingListener newGroup){
        for(GroupWatingListener group : groups){
            if(group.getGroupWating().getId().equals(newGroup.getGroupWating().getId())){
                return true;
            }
        }
        return false;
    }

}
