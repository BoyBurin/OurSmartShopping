package com.example.seniorproject.smartshopping.model.dao.group;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class PendingGroupMember {

    private String name;
    private String photoUrl;

    public PendingGroupMember(){

    }

    public PendingGroupMember(String name, String photoUrl){
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

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
