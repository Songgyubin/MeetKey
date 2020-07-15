package com.naver.naverspeech.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private ArrayList<View_My_Group> DataList;
    private LayoutInflater myinf = null;

    public ExpandableAdapter(Context context,int groupLay,int chlidLay,ArrayList<View_My_Group> DataList){
        this.DataList = DataList;
        this.groupLayout = groupLay;
        this.chlidLayout = chlidLay;
        this.context = context;
        this.myinf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = myinf.inflate(R.layout.list_grouprow, parent, false);
        }
        TextView groupName = (TextView)convertView.findViewById(R.id.group_name);
        groupName.setText(DataList.get(groupPosition).groupName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = myinf.inflate(R.layout.list_childrow, parent, false);
        }
        TextView childName = (TextView)convertView.findViewById(R.id.child_u_name);
        TextView childDepartment = (TextView)convertView.findViewById(R.id.child_u_department);
        TextView childCompany = (TextView)convertView.findViewById(R.id.child_u_company);
        TextView child_Position = (TextView)convertView.findViewById(R.id.child_u_position);
        ImageView child_image = (ImageView)convertView.findViewById(R.id.child_iconItem);
        Drawable childImage = context.getResources().getDrawable(R.drawable.account1);

        childName.setText(DataList.get(groupPosition).child_name.get(childPosition));
        childCompany.setText(DataList.get(groupPosition).child_company.get(childPosition));
        childDepartment.setText(DataList.get(groupPosition).child_department.get(childPosition));
        child_Position.setText(DataList.get(groupPosition).child_position1.get(childPosition));
        child_image.setImageDrawable(childImage);

        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return DataList.get(groupPosition).child_name.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return DataList.get(groupPosition).child_name.size();
    }

    @Override
    public View_My_Group getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return DataList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return DataList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

}


