package com.naver.naverspeech.client;

import java.util.ArrayList;

public class View_My_Group {
    public ArrayList<String> child_name;
    public ArrayList<String> child_department;
    public ArrayList<String> child_company;
    public ArrayList<String> child_position1;
    public String groupName;
    View_My_Group(String groupName){
        this.groupName = groupName;
        child_name = new ArrayList<String>();
        child_company = new ArrayList<String>();
        child_department = new ArrayList<String>();
        child_position1 = new ArrayList<String>();
    }
}
