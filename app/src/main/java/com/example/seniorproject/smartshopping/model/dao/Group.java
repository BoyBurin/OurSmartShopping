package com.example.seniorproject.smartshopping.model.dao;

import java.util.ArrayList;

/**
 * Created by boyburin on 9/6/2017 AD.
 */

public class Group {

    private String name;
    private ArrayList<String> member = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMember() {
        return member;
    }

    public void addMember(String userID) {
        member.add(userID);
    }

}
