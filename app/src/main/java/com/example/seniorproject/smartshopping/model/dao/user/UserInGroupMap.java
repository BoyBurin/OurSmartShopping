package com.example.seniorproject.smartshopping.model.dao.user;

/**
 * Created by boyburin on 9/9/2017 AD.
 */

public class UserInGroupMap {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String id;
    private UserInGroup user;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public UserInGroupMap(){

    }

    public UserInGroupMap(String id, UserInGroup user){
        this.id = id;
        this.user = user;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserInGroup getUser() {
        return user;
    }

    public void setUser(UserInGroup user) {
        this.user = user;
    }
}
