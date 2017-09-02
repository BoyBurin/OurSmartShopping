package com.example.seniorproject.smartshopping;

import android.app.Application;

import com.example.seniorproject.smartshopping.model.manager.Contextor;

/**
 * Created by boyburin on 8/28/2017 AD.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
