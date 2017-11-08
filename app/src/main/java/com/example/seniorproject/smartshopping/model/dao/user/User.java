package com.example.seniorproject.smartshopping.model.dao.user;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.manager.Contextor;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class User {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private String name;
    private String email;
    private String photoUrl;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public User() {

    }

    public User(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoRef) {
        this.photoUrl = photoRef;
    }
}
