package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 9/9/2017 AD.
 */

public class UserMap {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String id;
    private User user;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public UserMap(){

    }

    public UserMap(String id, User user){
        this.id = id;
        this.user = user;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
