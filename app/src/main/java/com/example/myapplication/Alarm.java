package com.example.myapplication;

import java.io.Serializable;

public class Alarm implements Serializable {

    private String phoneNo;
    private String shortenMsg;
    private String date;
    private String time;
    private long millis;


    public Alarm(String phoneNo, String time, String date, String shortenMsg) {
        this.phoneNo = phoneNo;
        this.shortenMsg = shortenMsg;
        this.time = time;
        this.date = date;
    }

    public Alarm(String phoneNo, String time, String date, String shortenMsg, long millis) {
        this.phoneNo = phoneNo;
        this.shortenMsg = shortenMsg;
        this.time = time;
        this.date = date;
        this.millis = millis;
    }

    public Alarm() {}


    public String getTime() { return time; }

    public void setTime() { this.time = time; }

    public String getDate() { return date; }

    public void setDate() { this.date = date; }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getShortenMsg() {
        return shortenMsg;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setShortenMsg(String shortenMsg) {
        this.shortenMsg = shortenMsg;
    }

    public void setMillis(long millis) { this.millis = millis; }

    public long getMillis() { return millis; }



}

