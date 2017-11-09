package com.example.seniorproject.smartshopping.model.manager.group;

import android.content.Context;


import com.example.seniorproject.smartshopping.model.dao.group.GroupWatingWithAction;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;


public class GroupWatingManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<GroupWatingWithAction> groups;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/



    private Context mContext;

    public GroupWatingManager() {

        mContext = Contextor.getInstance().getContext();
        groups = new ArrayList<GroupWatingWithAction>();
    }

    public GroupWatingWithAction getGroup(int index) {

        return groups.get(index);
    }


    public int getSize(){
        return groups.size();
    }

    public void addGroup(GroupWatingWithAction group){
        groups.add(group);
    }



    public ArrayList<GroupWatingWithAction> getGroups(){
        return groups;
    }


    public boolean isContain(GroupWatingWithAction newGroup){
        for(GroupWatingWithAction group : groups){
            if(group.getGroupWating().getId().equals(newGroup.getGroupWating().getId())){
                return true;
            }
        }
        return false;
    }

}