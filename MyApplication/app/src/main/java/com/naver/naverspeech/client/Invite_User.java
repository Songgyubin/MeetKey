package com.naver.naverspeech.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Invite_User extends AppCompatActivity {

    private SparseBooleanArray checkedlist_num;
    private int count;

    private ArrayList<Integer> invite_fnums = new ArrayList<Integer>();
    private ArrayList<String> invite_fnames = new ArrayList<String>();
    private ArrayList<String> invite_fcompanys = new ArrayList<String>();
    private ArrayList<String> invite_fdepartments = new ArrayList<String>();
    private ArrayList<String> invite_fpositions = new ArrayList<String>();
    private ArrayList<Drawable> invite_f_drawbles = new ArrayList<Drawable>();
    private int invite_fnum;
    private String invite_fname;
    private String invite_fcompany;
    private String invite_fdepartment;
    private String invite_fposition;
    private String invite_department;
    private Drawable invite_f_drawble;

    private Context context;
    private JSONArray jsonArray = new JSONArray();
    private JSONObject jsonObject = new JSONObject();


    private ArrayList<List_Item> data = new ArrayList<>();

    private List_Item list_item;
    private List_MakeGroupAdapter adapter;
    private ListView invite_user_list;
    private Button invite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_user);
        context =this;
        invite = (Button) findViewById(R.id.invite);

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                inviteUser();
                checkedlist_num = invite_user_list.getCheckedItemPositions();
                count = adapter.getCount();
        if(checkedlist_num ==null){
            Toast.makeText(getApplicationContext(),"추가할 친구를 선택해주세요",Toast.LENGTH_LONG).show();
        }
        for (int i = count-1;i>=0;i--){
            if(checkedlist_num.get(i)){
                Log.d("추가될 아이템"," "+data.get(i).getFnum());
                Log.d("체크넘"," "+i);

                invite_fnums.add (Integer.valueOf(data.get(i).getFnum()+1));
                invite_fnames.add(data.get(i).getUname());
                invite_fcompanys.add(data.get(i).getUcompany());
                invite_fdepartments.add (data.get(i).getUdepartment());
                invite_fpositions.add (data.get(i).getUposition());
            }
        }
        Intent intent1 = new Intent(Invite_User.this,Conference_pre.class);

        for (int i=0;i<invite_fnums.size();i++){

                try {
                    intent1.putExtra("fnums",invite_fnums);
                    intent1.putExtra("fnames",invite_fnames);
                    intent1.putExtra("fcompanys",invite_fcompanys);
                    intent1.putExtra("fdepartments",invite_fdepartments);
                    intent1.putExtra("fpositions",invite_fpositions);
                    Log.d("인바이트 노트",getIntent().getStringExtra("note"));

                } catch (Exception e) {

                    e.printStackTrace();
            }
        }
        intent1.putExtra("con",false);
        intent1.putExtra("subject",getIntent().getStringExtra("subject"));
        intent1.putExtra("room",getIntent().getStringExtra("room"));
        intent1.putExtra("note",getIntent().getStringExtra("note"));
        invite_user_list.clearChoices();
        adapter.notifyDataSetChanged();
                startActivity(intent1);
            }
        });
        invite_user_list = (ListView) findViewById(R.id.invite_user_list);
        printInviteFriends();
        adapter = new List_MakeGroupAdapter(this, R.layout.list_make_group_friends, data);
        invite_user_list.setAdapter(adapter);


    }
    private void printInviteFriends(){

        Cookie cookie = (Cookie) getApplication();
        final RequestQueue queue = Volley.newRequestQueue(this);
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
                                Log.e("초대 계정 불러오기 실패", response.getString("message"));
                                //Toast.makeText(getApplicationContext(),response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("초대 계정 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                System.out.println(response.get("data"));
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                for(int i =0; i<jsonArray.length();i++) {

                                    JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                                    invite_fname = dataJsonObj.getString("uname");
                                    invite_fnum = dataJsonObj.getInt("unum");
                                    invite_fcompany = dataJsonObj.getString("company");
                                    invite_department = dataJsonObj.getString("department");
                                    invite_f_drawble = context.getResources().getDrawable(R.drawable.account1);
                                    list_item = new List_Item(invite_fname,invite_fcompany,invite_department,invite_fnum,invite_f_drawble);
                                    data.add(list_item);
                                    adapter.notifyDataSetChanged();
                                }

                                //Toast.makeText(getApplicationContext(),"초대 계정 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch(JSONException e){
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("초대 계정 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("초대 계정 불러오기 실패", error.toString());
                        //Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }
//    private void inviteUser(){
//        Cookie cookie = (Cookie) getApplication();
//        checkedlist_num = invite_user_list.getCheckedItemPositions();
//        count = adapter.getCount();
//        if(checkedlist_num ==null){
//            //Toast.makeText(getApplicationContext(),"추가할 친구를 선택해주세요",//Toast.LENGTH_LONG).show();
//        }
//        for (int i = count-1;i>=0;i--){
//            if(checkedlist_num.get(i)){
//                Log.d("추가될 아이템"," "+data.get(i).getFnum());
//                Log.d("체크넘"," "+i);
//                member[i] = data.get(i).getFnum()+1;
//
//            }
//        }
//        for (int i=0;i<member.length;i++){
//            if(member[i]!=0) {
//                try {
//                    jsonArray.put(member[i]-1);
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//                System.out.println("멤버 " + member[i]);
//            }
//        }
//        try {
//            jsonObject.put("member", jsonArray);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        invite_user_list.clearChoices();
//        adapter.notifyDataSetChanged();
//        Log.d("으어"," "+jsonObject.toString());
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("unum", String.valueOf(cookie.getCookie()));
//        params.put("gname", group_names);
//        params.put("member",jsonObject.toString());
//        Log.d("유넘",String.valueOf(cookie.getCookie()));
//        Log.d("쥐넘",group_names);
//        //멤버 넘기기
//        final RequestQueue queue = Volley.newRequestQueue(Invite_User.this);
//        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/group";
//
//        // 일단 시작
//        queue.start();
//
//        // 요청 만들기
//        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    // 제대로 된 응답이 들어왔을 시
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (response.has("message")) { // 메세지 가지고 있음 에러난거
//                                // 오류를 사용자에게 알리도록 고칠 것.
//                                // message에 있는 내용을 알릴 것.
//                                Log.e("그룹추가 실패", response.getString("message"));
//                                //Toast.makeText(getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
//                            } else { // 메세지 없으면 성공한거
//                                Log.v("그룹추가 성공", "success");
//                                //Toast.makeText(getApplicationContext(), "그룹추가 성공", //Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            // 오류를 사용자에게 알리도록 고칠 것.
//                            // 알 수 없는 원인.
//                            // e.printStackTrace();
//                            //Toast.makeText(getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
//                    public void onErrorResponse(VolleyError error) {
//                        // 오류를 사용자에게 알리도록 고칠 것.
//                        Log.e("그룹추가 실패", "인터넷 연결을 확인해주세요.");
//                        //Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
//                    }
//                });
//        // 요청 큐에 추가하기
//        queue.add(jsonObjectRequest);
//        Intent intent = new Intent(Invite_User.this, MainActivity.class);
//        startActivity(intent);
//    }
}
