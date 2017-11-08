package com.example.seniorproject.smartshopping.model.dao.user;


public class UserInGroup {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String name;
    private String photoUrl;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public UserInGroup() {

    }

    public UserInGroup(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoRef) {
        this.photoUrl = photoRef;
    }
}
