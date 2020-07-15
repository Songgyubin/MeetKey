package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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

import androidx.appcompat.app.AppCompatActivity;


public class Make_Groups extends AppCompatActivity {

    private EditText groupname;
    private Button addgroups;
    private ImageButton make_groups_gohome;

    private Context context;
    private List_Item list_item;
    private ArrayList<List_Item> data = new ArrayList<>();
    private List_MakeGroupAdapter adapter;
    public ListView makeGroup_friends;

    private SparseBooleanArray checkedlist_num;
    public static String group_names;
    private String uname;
    private String ucompany;
    private String udepartment;
    private String uposition;

    private int fnum;
    private Drawable f_drawble;
    private int count;
    private String [] member = new String[300];
    private int [] member1 = new int[300];
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_groups);

        context = this;
        groupname = (EditText) findViewById(R.id.groupname);
        addgroups = (Button) findViewById(R.id.addgroup);
        adapter = new List_MakeGroupAdapter(this, R.layout.list_make_group_friends, data);

        makeGroup_friends = (ListView) findViewById(R.id.makeGroup_friends);
        make_groups_gohome = (ImageButton) findViewById(R.id.make_groups_gohome);
        make_groups_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Make_Groups.this,MainActivity.class);
                startActivity(intent);
            }
        });

        printGroupFriends();
        makeGroup_friends.setAdapter(adapter);

        addgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group_names = groupname.getText().toString();
                Cookie cookie = (Cookie) getApplication();
                checkedlist_num = makeGroup_friends.getCheckedItemPositions();
                count = adapter.getCount();
                if(checkedlist_num ==null){
                    //Toast.makeText(getApplicationContext(),"추가할 친구를 선택해주세요",//Toast.LENGTH_LONG).show();
                }
                for (int i = count-1;i>=0;i--){
                    if(checkedlist_num.get(i)){
                        Log.d("추가될 아이템"," "+data.get(i).getFnum());
                        Log.d("체크넘"," "+i);
                        member1[i] = data.get(i).getFnum()+1;

                    }
                }
                for (int i=0;i<member1.length;i++){
                    if(member1[i]!=0) {
                        try {
                            jsonArray.put(member1[i]-1);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        System.out.println("멤버 " + member1[i]);
                    }
                }
                try {
                    jsonObject.put("member", jsonArray);
                }catch (Exception e){
                    e.printStackTrace();
                }
                makeGroup_friends.clearChoices();
                adapter.notifyDataSetChanged();
                Log.d("으어"," "+jsonObject.toString());
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("unum", String.valueOf(cookie.getCookie()));
                params.put("gname", group_names);
                params.put("member",jsonObject.toString());
                Log.d("유넘",String.valueOf(cookie.getCookie()));
                Log.d("쥐네임",group_names);

            //멤버 넘기기
                final RequestQueue queue = Volley.newRequestQueue(Make_Groups.this);
                final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/group";

                // 일단 시작
                queue.start();

                // 요청 만들기
                // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            // 제대로 된 응답이 들어왔을 시
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.has("message")) { // 메세지 가지고 있음 에러난거
                                        // 오류를 사용자에게 알리도록 고칠 것.
                                        // message에 있는 내용을 알릴 것.
                                        Log.e("그룹추가 실패", response.getString("message"));
                                        //Toast.makeText(getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
                                    } else { // 메세지 없으면 성공한거
                                        Log.v("그룹추가 성공", "success");
                                        //Toast.makeText(getApplicationContext(), "그룹추가 성공", //Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // 오류를 사용자에게 알리도록 고칠 것.
                                    // 알 수 없는 원인.
                                    // e.printStackTrace();
                                    //Toast.makeText(getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                            public void onErrorResponse(VolleyError error) {
                                // 오류를 사용자에게 알리도록 고칠 것.
                                Log.e("그룹추가 실패", "인터넷 연결을 확인해주세요.");
                                //Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                            }
                        });
                // 요청 큐에 추가하기
                queue.add(jsonObjectRequest);
                Intent intent = new Intent(Make_Groups.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    protected void printGroupFriends() {
        Cookie cookie = (Cookie) getApplication();
        final RequestQueue queue = Volley.newRequestQueue(Make_Groups.this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/favorite?unum=" + cookie.getCookie();
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
                                Log.e("즐겨찾는 계정 불러오기 실패", response.getString("message"));
                                //Toast.makeText(getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("즐겨찾는 계정 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                System.out.println(response.get("data"));
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                                    uname = dataJsonObj.getString("uname");
                                    fnum = dataJsonObj.getInt("unum");
                                    ucompany = dataJsonObj.getString("company");
                                    udepartment = dataJsonObj.getString("department");
                                    uposition = dataJsonObj.getString("position");

                                    f_drawble = context.getResources().getDrawable(R.drawable.account1);
                                    list_item = new List_Item(uname,ucompany,udepartment,fnum,f_drawble,uposition);
                                    data.add(list_item);
                                    adapter.notifyDataSetChanged();
                                    Log.d("그룹불러올때 아이템"," "+data.get(i).getFnum());
                                }

                                //Toast.makeText(getApplicationContext(), "즐겨찾는 계정 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
                            //Toast.makeText(getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }

}
