package com.naver.naverspeech.client;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class LIst_Edit_FriendsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<List_Item> data;
    private int layout;
    private String del_f_info;

    Join_Add joinadd;

    public LIst_Edit_FriendsAdapter(Context context, int layout, ArrayList<List_Item> data){
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

        final List_Item listviewitem =data.get(position);
        TextView del_friends = (TextView)convertView.findViewById(android.R.id.text1);
        del_f_info = listviewitem.getUcompany() +"/"+ listviewitem.getUdepartment() +"/"+ listviewitem.getUposition() +"/"+ listviewitem.getUname();
        del_friends.setText(del_f_info);
        del_friends.setCompoundDrawables(listviewitem.get_f_drawble(),null,null,null);

//        delete = (ImageButton)convertView.findViewById(R.id.u_delete);
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("포지션은: "," "+data.get(position));
//                data.remove(data.get(position));
//                System.out.println("얘 이름은+ "+ listviewitem.getUname());
//                notifyDataSetChanged();
//
//            }
//        });

        return convertView;
    }
}

