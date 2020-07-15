package com.naver.naverspeech.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class List_Reservation_MemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<List_Item> data;
    private int layout;


    public List_Reservation_MemAdapter(Context context, int layout, ArrayList<List_Item> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        final List_Item listviewitem = data.get(position);

        TextView mem_uname = (TextView) convertView.findViewById(R.id.mem_u_name);
        mem_uname.setText(listviewitem.getUname());

        TextView mem_company = (TextView) convertView.findViewById(R.id.mem_u_company);
        mem_company.setText(listviewitem.getUcompany());

        TextView mem_department = (TextView) convertView.findViewById(R.id.mem_u_department);
        mem_department.setText(listviewitem.getUdepartment());

        return convertView;
    }
}
