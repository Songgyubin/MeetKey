package com.naver.naverspeech.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class History_Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<List_m_Item> data;
    private int layout;

    public History_Adapter(Context context, int layout, ArrayList<List_m_Item> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout=layout;
        this.data=data;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getName();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        final List_m_Item listviewitem = data.get(position);
        TextView summarize = (TextView) convertView.findViewById(R.id.history_focus_subject);
        int last = listviewitem.getFocus_subject().length();
        summarize.setText(listviewitem.getFocus_subject().substring(1,last-1));
        return convertView;
    }
}
