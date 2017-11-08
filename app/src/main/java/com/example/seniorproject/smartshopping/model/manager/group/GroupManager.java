package com.example.seniorproject.smartshopping.model.manager.group;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.group.GroupList;
import com.example.seniorproject.smartshopping.model.dao.group.GroupMap;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;


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

    public ArrayList<GroupList> getGroupsWithoutCurrent(){
        ArrayList<GroupList> newGroups = new ArrayList<>();

        for(GroupList group : groups){
            if(!currentGroup.getId().equals(group.getId())){
                newGroups.add(group);
            }
        }
        return newGroups;
    }

    public GroupList getGroupWithoutCurrent(int position){
        ArrayList<GroupList> newGroups = new ArrayList<>();

        for(GroupList group : groups){
            if(!currentGroup.getId().equals(group.getId())){
                newGroups.add(group);
            }
        }
        return newGroups.get(position);
    }

    public void deleteGroup(GroupList groupList){
        for(int i = 0 ; i < groups.size() ; i++){
            if(groups.get(i).getId().equals(groupList.getId())){
                groups.remove(i);
            }
        }
    }

}
