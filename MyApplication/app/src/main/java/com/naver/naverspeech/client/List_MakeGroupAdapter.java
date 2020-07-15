package com.naver.naverspeech.client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class List_MakeGroupAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<List_Item> data = new ArrayList<List_Item>();
    private int layout;

    Join_Add joinadd;

    public static ImageButton delete;
    ListView make_group_listview;
    private CheckBox Make_Group_Check_cb;
    private Make_Group_Check make_group_check;
    private Make_Groups f = new Make_Groups();

    public List_MakeGroupAdapter() {
    }

    public List_MakeGroupAdapter(Context context, int layout, ArrayList<List_Item> data) {
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


        Make_Group_Check_cb = (CheckBox) convertView.findViewById(R.id.make_group_check);
//        make_group_listview = (ListView)f.findViewById(R.id.makeGroup_friends);
        TextView make_group_username = (TextView) convertView.findViewById(R.id.make_group_u_name);
        make_group_username.setText(listviewitem.getUname());

        TextView make_group_usercompany = (TextView) convertView.findViewById(R.id.make_group_u_company);
        make_group_usercompany.setText(listviewitem.getUcompany());

        TextView make_group_userdepartment = (TextView) convertView.findViewById(R.id.make_group_u_department);
        make_group_userdepartment.setText(listviewitem.getUdepartment());

        ImageView make_group_f_drawble = (ImageView) convertView.findViewById(R.id.make_group_iconItem);
        make_group_f_drawble.setImageDrawable(listviewitem.get_f_drawble());

        TextView make_group_uposition = (TextView) convertView.findViewById(R.id.make_group_u_position);
        make_group_uposition.setText(listviewitem.getUposition());

        Make_Group_Check_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("체크박스의 포지션"," "+data.get(position).getUname());
            }
        });

        return convertView;
    }
}
