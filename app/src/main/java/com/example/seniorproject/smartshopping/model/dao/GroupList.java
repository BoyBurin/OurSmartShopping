package com.example.seniorproject.smartshopping.model.dao;

/**
 * Created by boyburin on 10/25/2017 AD.
 */

public class GroupList {
    private String id;
    private String name;
    private String photoUrl;

    public GroupList(){}

    public GroupList(String id, String name, String photoUrl){
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
