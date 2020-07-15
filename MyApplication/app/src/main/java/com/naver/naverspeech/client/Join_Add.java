package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Join_Add extends Fragment {

    private int fnum;

    private Button show_friends;
    private Button show_group;
    private Button viewgroup;
    private View view;
    private View list_grouprow_view;
    private Activity root;

    // 계정
    private ListView friends_list;
    private ArrayList<List_Item> data = new ArrayList<>();
    private List_friendsAdapter adapter;
    private List_Item list_item;

    // 그룹
    private ArrayList<View_My_Group> DataList = null;
    //    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContentname = null;
    private ArrayList<String> mChildListContentcompany = null;
    private ArrayList<String> mChildListContentdepartment = null;
    private ArrayList<String> mChildListContentposition = null;
    private ExpandableListView group_list;
    private View_My_Group MyGroup;
    private ArrayList<String> GroupnameString = new ArrayList<String>();
    private ArrayList<String> G_usernameString = new ArrayList<String>();
    private ArrayList<String> G_usercompanyString = new ArrayList<String>();
    private ArrayList<String> G_userdepartmentString = new ArrayList<String>();
    private ArrayList<String> G_userpositionString = new ArrayList<String>();
    private ImageView iv_image;


    int gnum;
    String groupname;
    String g_uname;
    String g_u_company;
    String g_u_department;
    String g_u_position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.join__add,container,false);
        list_grouprow_view = inflater.inflate(R.layout.list_grouprow,container,false);
        iv_image = (ImageView) list_grouprow_view.findViewById(R.id.iv_image);

        root = getActivity();
        setLayout();

        // 계정
        friends_list = (ListView) view.findViewById(R.id.friends_list);
        adapter = new List_friendsAdapter(view.getContext(),R.layout.list_friends,data);

        // 그룹
        DataList = new ArrayList<View_My_Group>();
//        mChildList = new ArrayList<ArrayList<String>>();
        mChildListContentname = new ArrayList<String>();
        mChildListContentcompany = new ArrayList<String>();
        mChildListContentdepartment = new ArrayList<String>();
        mChildListContentposition = new ArrayList<String>();




        show_friends = (Button) view.findViewById(R.id.show_friends);
        show_group = (Button) view.findViewById(R.id.show_group);

        show_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_friends.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.toggle_button_friend_shape));
                show_friends.setTextColor(Color.parseColor("#2a3f54"));
                friends_list.setVisibility(View.VISIBLE);
                show_group.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.toggle_button_group_shape));
                show_group.setTextColor(Color.parseColor("#ffffff"));
                group_list.setVisibility(View.GONE);
            }
        });
        show_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_friends.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.toggle_button_group_shape2));
                show_friends.setTextColor(Color.parseColor("#ffffff"));
                friends_list.setVisibility(View.GONE);
                show_group.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.toggle_button_friend_shape2));
                show_group.setTextColor(Color.parseColor("#2a3f54"));
                group_list.setVisibility(View.VISIBLE);
            }
        });

        printFavorite();
        printGroup();
        friends_list.setAdapter(adapter);

        group_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Toast.makeText(view.getContext(), "g click = " + groupPosition, Toast.LENGTH_SHORT).show();

                if (parent.isGroupExpanded(groupPosition) == false) {
                    parent.expandGroup(groupPosition);
                    iv_image.setImageResource(R.drawable.arrow_down_35);

                } else {
                    parent.collapseGroup(groupPosition);
                    iv_image.setImageResource(R.drawable.arrow_up_35);
                }
                return true;
            }
        });


        group_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                //Toast.makeText(getApplicationContext(), "c click = " + childPosition,
                //Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        group_list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

                //Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition,
                //Toast.LENGTH_SHORT).show();
            }
        });

        group_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                //Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition,
                //Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }

    private void printFavorite() {
        Cookie cookie = (Cookie) root.getApplication();
        final RequestQueue queue = Volley.newRequestQueue(root.getApplicationContext());
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/favorite?unum="+cookie.getCookie();
        Log.i("url", url);

        // 일단 시작
        queue.start();

        // 요청 만들기
        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    // 제대로 된 응답이 들어왔을 시
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.has("message")) { // 메세지 가지고 있음 에러난거
                                // 오류를 사용자에게 알리도록 고칠 것.
                                // message에 있는 내용을 알릴 것.
                                Log.e("즐겨찾는 계정 불러오기 실패", response.getString("message"));
//                                //Toast.makeText(root,response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("즐겨찾는 계정 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                System.out.println(response.get("data"));
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                for(int i =0; i<jsonArray.length();i++) {

                                    JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                                    String uname = dataJsonObj.getString("uname");
                                    String ucompany = dataJsonObj.getString("company");
                                    String udepartment = dataJsonObj.getString("department");
                                    String uposition = dataJsonObj.getString("position");
                                    fnum = dataJsonObj.getInt("unum");
                                    list_item = new List_Item(uname, ucompany, udepartment, fnum, uposition);
                                    data.add(list_item);
                                    adapter.notifyDataSetChanged();
                                }

//                                //Toast.makeText(root,"즐겨찾는 계정 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch(JSONException e){
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
//                            //Toast.makeText(root,"알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("즐겨찾는 계정 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("즐겨찾는 계정 불러오기 실패", error.toString());
//                        //Toast.makeText(root,"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }
    private void printGroup(){
        Cookie cookie = (Cookie) root.getApplication();
        final RequestQueue queue = Volley.newRequestQueue(view.getContext());
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/group?unum=" + cookie.getCookie();
        Log.i("url", url);

        // 일단 시작
        queue.start();

        // 요청 만들기
        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    // 제대로 된 응답이 들어왔을 시
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("message")) { // 메세지 가지고 있음 에러난거
                                // 오류를 사용자에게 알리도록 고칠 것.
                                // message에 있는 내용을 알릴 것.
                                Log.e("그룹 불러오기 실패", response.getString("message"));
                                //Toast.makeText(getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();

                            } else { // 메세지 없으면 성공한거
                                Log.v("그룹 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                JSONArray jsonArray = new JSONArray(response.names().toString());

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    gnum = jsonArray.getInt(i);
                                    JSONArray jsonArrayG = response.getJSONArray(String.valueOf(gnum));
                                    groupname = jsonArrayG.getJSONObject(0).getString("gname");
                                    GroupnameString.add(groupname);
                                    MyGroup = new View_My_Group(GroupnameString.get(i));
                                    Log.d("그룹이름",groupname);
                                    for (int j = 0; j < jsonArrayG.length(); j++) {
                                        JSONObject jsonObjectG = jsonArrayG.getJSONObject(j);
                                        g_uname = jsonObjectG.getString("uname");
                                        g_u_company = jsonObjectG.getString("company");
                                        g_u_department = jsonObjectG.getString("department");
                                        g_u_position = jsonObjectG.getString("position");
                                        Log.d("조인애드","되냐");
                                        Log.d("조인애드 포지션",g_u_position);

                                        G_usernameString.add(g_uname);
                                        G_usercompanyString.add(g_u_company);
                                        G_userdepartmentString.add(g_u_department);
                                        G_userpositionString.add(g_u_position);
                                        Log.d("조인애드 포지션2",G_userpositionString.get(0));
                                        mChildListContentname.add(G_usernameString.get(j));
                                        mChildListContentcompany.add(G_usercompanyString.get(i));
                                        mChildListContentdepartment.add(G_userdepartmentString.get(i));
                                        mChildListContentposition.add(G_userpositionString.get(i));

                                        MyGroup.child_name.add(mChildListContentname.get(j));
                                        MyGroup.child_company.add(mChildListContentcompany.get(i));
                                        MyGroup.child_department.add(mChildListContentdepartment.get(i));
                                        MyGroup.child_position1.add(mChildListContentposition.get(i));

                                        Log.d("유저가 들어갈 그룹이름",G_usernameString.get(j));
                                    }
                                    DataList.add(MyGroup);
                                }
                            }
                            //Toast.makeText(getApplicationContext(), "그룹 불러오기 성공", //Toast.LENGTH_LONG).show();
                            group_list.setAdapter(new ExpandableAdapter(view.getContext(),R.layout.list_grouprow,R.layout.list_childrow, DataList));
                        } catch (JSONException e) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            e.printStackTrace();

                            //Toast.makeText(getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("그룹 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("그룹 불러오기 실패", error.toString());
                        //Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });

        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }
    private void setLayout(){
        group_list = (ExpandableListView) view.findViewById(R.id.group_list);
    }
}