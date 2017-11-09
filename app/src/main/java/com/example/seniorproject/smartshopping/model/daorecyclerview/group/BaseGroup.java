package com.example.seniorproject.smartshopping.model.daorecyclerview.group;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class BaseGroup {

    public static final int TITTLE_NAME = 0;
    public static final int GROUP_DETAIL = 1;
    public static final int PENDING_GROUP_MEMBER = 2;
    public static final int GROUP_MEMBER = 3;

    private int type;

    public BaseGroup(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

}
