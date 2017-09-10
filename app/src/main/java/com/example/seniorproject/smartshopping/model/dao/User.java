package com.example.seniorproject.smartshopping.model.dao;

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
    private String photoRef;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public User(){

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

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }
}
