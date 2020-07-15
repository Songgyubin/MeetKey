package com.naver.naverspeech.client;

import android.graphics.drawable.Drawable;

public class List_m_Item {

    private String name;
    private String made;
    private String mname;
    private String time;
    private String where;
    private String date;
    private String mid;
    private String focus_subject;
    private String alldate;
    private Drawable m_drawble = null;

    public List_m_Item(String focus_subject){
        this.focus_subject = focus_subject;
    }
    public List_m_Item(String mid,String made, String mname, String time, String where, String date,String alldate){
        this.mid = mid;
        this.made = made;
        this.mname = mname;
        this.time = time;
        this.where = where;
        this.date = date;
        this.alldate = alldate;
    }


    // 회의목록
    public String getName(){
        return name;
    }

    public String getMade() { return made; }

    public String getMname() {return mname;}

    public String getTime() {return time;}

    public String getWhere() {return where;}

    public String getDate() {return date;}

    public String getMid() {return mid;}
    public String getFocus_subject(){return focus_subject;}
    public String getAlldate() {return alldate;}
}
