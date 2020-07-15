package com.naver.naverspeech.client;

import java.util.ArrayList;

public class Position {

    //Properties of Position
    public String position;
    public String image;
    public ArrayList<String> players = new ArrayList<String>();

    public Position(String position){
        this.position = position;
    }

    public String toString () {
        return position;
    }

}

