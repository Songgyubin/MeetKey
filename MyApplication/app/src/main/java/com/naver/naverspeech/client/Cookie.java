package com.naver.naverspeech.client;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Cookie extends Application {
    private int unum;
    private String uname;
    private String ucompany;
    private String udepartment;
    private String uposition;
    private Drawable uImage;
    private int flag = 1;
    private String groupname;
    private Context context = this;
    private String subject;
    private String room;
    private String year;
    private String month;
    private String day;
    private String dow;
    private String mid;
    private String note;
    private String time;
    private ArrayList<Integer> invited_fnums = new ArrayList<>();

    private String date;
    private String mname;

    public int getCookie() {
        return unum;
    }

    public void setCookie(int unum) {
        this.unum = unum;
    }

    public void setUserInfo(String uname, String ucompany, String udepartment, String uposition, Drawable uImage) {
        this.uname = uname;
        this.ucompany = ucompany;
        this.udepartment = udepartment;
        this.uposition = uposition;
        this.uImage = uImage;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setMeetingInfo(String subject, String room, String year, String month, String day, String dow, String time, String note, ArrayList<Integer> invited_fnums) {
        this.subject = subject;
        this.room = room;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dow = dow;
        this.time = time;
        this.note = note;
        this.invited_fnums = invited_fnums;
    }
    public void setMeetingTime(String date, String mname){
        this.date = date;
        this.mname = mname;
    }
    public String getMeetingTime(){return date;}
    public String getMeetingTime2(){return mname;}
    public String getUname() {
        return uname;
    }

    public String getUcompany() {
        return ucompany;
    }

    public String getUdepartment() {
        return udepartment;
    }

    public String getUposition() {
        return uposition;
    }

    public Drawable getUimage() {
        uImage = context.getResources().getDrawable(R.drawable.account1);
        return uImage;
    }

    public String getMid() {
        return mid;
    }

    public String getSubject() {
        return subject;
    }

    public String getRoom() {
        return room;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getDow() {
        return dow;
    }

    public String getTime() {
        return time;
    }

    public String getNote() {
        return note;
    }

    public ArrayList<Integer> getInvited_fnums() {
        return invited_fnums;
    }


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getGroupName() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}