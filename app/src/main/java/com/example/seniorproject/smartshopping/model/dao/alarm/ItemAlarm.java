package com.example.seniorproject.smartshopping.model.dao.alarm;

/**
 * Created by boyburin on 10/30/2017 AD.
 */

public class ItemAlarm {
    private String day;
    private int hour;
    private int minute;
    private long index;
    private Long timeInMillis;

    public ItemAlarm(String day, int hour, int minute,  Long timeInMillis, long index) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.timeInMillis = timeInMillis;
        this.index = index;
    }

    public ItemAlarm(){

    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
