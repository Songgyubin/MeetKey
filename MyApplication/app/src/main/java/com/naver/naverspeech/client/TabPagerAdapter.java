package com.naver.naverspeech.client;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        //Returning the current tabs
        switch (position){
            case 0: //회의록
                View_History view_history = new View_History();
                return view_history;
            case 1: // 계정&그룹
                Join_Add join_add = new Join_Add();
                return join_add;
            case 2:
                Reservation_Conference reservation_conference = new Reservation_Conference();
                return reservation_conference;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
