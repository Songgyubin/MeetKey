package com.naver.naverspeech.client;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class List_Reservation_ConAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<List_m_Item> data;
    private int layout;


    public List_Reservation_ConAdapter(Context context, int layout, ArrayList<List_m_Item> data) {
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

        final List_m_Item listviewitem =data.get(position);

        TextView reservation_made = (TextView)convertView.findViewById(R.id.reservation_con_made);
        reservation_made.setText(listviewitem.getMade());

        TextView reservation_where = (TextView)convertView.findViewById(R.id.reservation_con_where);
        reservation_where.setText(listviewitem.getWhere());

        TextView reservation_mname = (TextView)convertView.findViewById(R.id.reservation_con_mname);
        reservation_mname.setText(listviewitem.getMname());

        TextView reservation_time = (TextView)convertView.findViewById(R.id.reservation_con_time);
        reservation_time.setText(listviewitem.getTime());

        TextView resvation_date = (TextView)convertView.findViewById(R.id.resvation_con_date);
        resvation_date.setText(listviewitem.getDate());

        if (convertView.callOnClick()) {
            convertView.setBackgroundColor(Color.parseColor("#7de2fc"));
        }


        return convertView;
    }
}
