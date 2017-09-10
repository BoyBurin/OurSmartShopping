package com.example.seniorproject.smartshopping.model.manager;

import android.content.Context;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SingletonTemplate {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private static SingletonTemplate instance;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/


    public static SingletonTemplate getInstance() {
        if (instance == null)
            instance = new SingletonTemplate();
        return instance;
    }

    private Context mContext;

    private SingletonTemplate() {
        mContext = Contextor.getInstance().getContext();
    }

}
