package com.example.seniorproject.smartshopping.controller.boardcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;

/**
 * Created by boyburin on 10/30/2017 AD.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("item");
        ItemInventory itemInventory = (ItemInventory) bundle.get("item");

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyId = 1;
        String channelId = "some_channel_id";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        builder.setContentTitle(itemInventory.getName() + " จากกลุ่ม " + GroupManager.getInstance().getCurrentGroup().getGroup().getName())
                .setContentText("You've received new messages!")
                .setWhen(System.currentTimeMillis())
                .setContentText("ตอนนี้เหลืออยู่ " + itemInventory.getAmount() + " "+ itemInventory.getUnit())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.launcher_icon)
                .setContentInfo("Info");


        Toast.makeText(context, "Yeah Cogratulation", Toast.LENGTH_LONG).show();
        notificationManager.notify(1, builder.build());
    }
}
