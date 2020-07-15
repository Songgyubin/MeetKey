package com.naver.naverspeech.client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class List_friendsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<List_Item> data;
    private int layout;

    public List_friendsAdapter(Context context, int layout, ArrayList<List_Item> data){
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
        TextView username = (TextView)convertView.findViewById(R.id.u_name);
        username.setText(listviewitem.getUname());

        TextView usercompany = (TextView)convertView.findViewById(R.id.u_company);
        usercompany.setText(listviewitem.getUcompany());

        TextView userdepartment = (TextView)convertView.findViewById(R.id.u_department);
        userdepartment.setText(listviewitem.getUdepartment());

        TextView userposition = (TextView)convertView.findViewById(R.id.u_position);
        userposition.setText(listviewitem.getUposition());

        return convertView;
    }
}
