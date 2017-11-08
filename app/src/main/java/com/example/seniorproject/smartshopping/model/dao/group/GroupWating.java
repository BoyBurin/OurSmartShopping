package com.example.seniorproject.smartshopping.model.dao.group;

/**
 * Created by boyburin on 10/31/2017 AD.
 */

public class GroupWating {
    private String id;
    private String name;
    private String photoUrl;

    public GroupWating(String id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public GroupWating(){}

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
