package com.example.seniorproject.smartshopping.model.manager.group;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.group.GroupWatingWithAction;
import com.example.seniorproject.smartshopping.model.dao.group.PendingGroupMember;
import com.example.seniorproject.smartshopping.model.dao.group.PendingGroupMemberWithAction;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;


public class PendingGroupMemberManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<PendingGroupMemberWithAction> members;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/



    private Context mContext;

    public PendingGroupMemberManager() {

        mContext = Contextor.getInstance().getContext();
        members = new ArrayList<PendingGroupMemberWithAction>();
    }

    public PendingGroupMemberWithAction getGroup(int index) {

        return members.get(index);
    }


    public int getSize(){
        return members.size();
    }

    public void addGroup(PendingGroupMemberWithAction group){
        members.add(group);
    }



    public ArrayList<PendingGroupMemberWithAction> getGroups(){
        return members;
    }

    public void removePendingGroupMember(String id){
        for(int i = 0 ; i < members.size() ; i++){
            if(members.get(i).getId().equals(id)){
                members.remove(i);
                break;
            }
        }
    }



}
