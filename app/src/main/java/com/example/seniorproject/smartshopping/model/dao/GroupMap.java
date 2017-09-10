package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 9/9/2017 AD.
 */

public class GroupMap {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String id;
    private Group group;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public GroupMap(){

    }

    public GroupMap(String id, Group group){
        this.id = id;
        this.group = group;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
