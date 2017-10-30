package com.example.seniorproject.smartshopping.model.dao.alarm;

import android.view.View;

/**
 * Created by boyburin on 10/30/2017 AD.
 */

public class ItemAlarmIdAction {

    private ItemAlarm itemAlarm;
    private String id;
    private View.OnClickListener deleteListener;

    public ItemAlarmIdAction(ItemAlarm itemAlarm, String id, View.OnClickListener deleteListener) {
        this.itemAlarm = itemAlarm;
        this.id = id;
        this.deleteListener = deleteListener;
    }

    public ItemAlarmIdAction(){

    }

    public ItemAlarm getItemAlarm() {
        return itemAlarm;
    }

    public void setItemAlarm(ItemAlarm itemAlarm) {
        this.itemAlarm = itemAlarm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public View.OnClickListener getDeleteListener() {
        return deleteListener;
    }

    public void setDeleteListener(View.OnClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }
}
