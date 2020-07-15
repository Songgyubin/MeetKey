package com.naver.naverspeech.client;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class List_Item {

    // 친구목록
    private String uname = null;
    private String ucompany = null;
    private String udepartment = null;
    private int fnum = 0;
    private Drawable f_drawble = null;
    private String uposition = null;

    private int icon;
    private String name = null;

    // 회의목록


    public List_Item(String uname){
        this.uname = uname;
    }
    public List_Item(String uname, String ucompany, String udepartment, int fnum , Drawable f_drawble,String uposition){
        this.uname = uname;
        this.ucompany = ucompany;
        this.udepartment = udepartment;
        this.fnum = fnum;
        this.f_drawble = f_drawble;
        this.uposition = uposition;
    }
    public List_Item(String uname, String ucompany, String udepartment, int fnum , Drawable f_drawble){
        this.uname = uname;
        this.ucompany = ucompany;
        this.udepartment = udepartment;
        this.fnum = fnum;
        this.f_drawble = f_drawble;

    }
    public List_Item(String uname, String ucompany, String udepartment, int fnum, String uposition){
        this.uname = uname;
        this.ucompany = ucompany;
        this.udepartment = udepartment;
        this.fnum = fnum;
        this.uposition = uposition;
    }
    public List_Item(String uname, Drawable f_drawble, int fnum, String ucompany, String udepartment, String uposition){
        this.uname = uname;
        this.f_drawble = f_drawble;
        this.fnum = fnum;
        this.ucompany = ucompany;
        this.udepartment = udepartment;
        this.uposition = uposition;
    }
    public List_Item(String uname, String ucompany, String udepartment){
        this.uname = uname;
        this.ucompany = ucompany;
        this.udepartment = udepartment;
    }

    // 친구목록
    public int getIcon(){
        return icon;
    }

    public String getName(){
        return name;
    }

    public String getUname() {
        return uname;
    }

    public String getUcompany() {
        return ucompany;
    }

    public String getUdepartment() {
        return udepartment;
    }

    public Drawable get_f_drawble() { return f_drawble; }

    public String getUposition() { return uposition; }

    public int getFnum() {return fnum;}



//    public String getFocus_subject() {
//        if (focus_subject.size()!=0){
//
//        }
//    }
}
