package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Join_Meeting extends AppCompatActivity {

    private ListView reservation_conference_list;
    private ListView reservation_member_list;
    private ArrayList<List_Item> data = new ArrayList<>();
    private ArrayList<List_Item> data1 = new ArrayList<>();
    private ArrayList<List_m_Item> data2 = new ArrayList<>();
    private List_Reservation_ConAdapter Reservation_adapter;
    private List_Reservation_MemAdapter Reservation_mem_adapter;
    private List_Item list_item;
    private List_m_Item list_m_item;
    private Button reservation_con_start;
    private LinearLayout join_conference_layout;
    private TextView join_conference_text;
    private ImageButton join_conference_gohome;

    private ArrayList<Integer> member = new ArrayList<>();
    private ArrayList<Integer> num_on = new ArrayList<>();
    private ArrayList<String> mid = new ArrayList<>();
    private ArrayList<String> mname = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> where = new ArrayList<>();
    private ArrayList<String> made = new ArrayList<>();
    private ArrayList<String> memo = new ArrayList<>();
    private ArrayList<Integer> __v = new ArrayList<>();
    private ArrayList<String> start = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> mem_uname = new ArrayList<>();
    private ArrayList<String> mem_company = new ArrayList<>();
    private ArrayList<String> mem_department = new ArrayList<>();
    private boolean List_flag = false;
    private String Valid_uname;
    private int con_position = 0;
    private SimpleDateFormat s = new SimpleDateFormat("MM");
    private SimpleDateFormat s1 = new SimpleDateFormat("dd");
    private SimpleDateFormat s2 = new SimpleDateFormat("EE");
    private SimpleDateFormat s3 = new SimpleDateFormat("yyyy");
    private String smon;
    private String sday;
    private String sdow;
    private String syear;
    private String choice_mid;
    private String choice_mname;
    private String choice_mwhere;
    private String choice_made;
    private String choice_syear;
    private String choice_smon;
    private String choice_sday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_conference);
        reservation_con_start =  findViewById(R.id.reservation_con_start);
        reservation_conference_list =  findViewById(R.id.reservation_conference_list);
        Reservation_adapter = new List_Reservation_ConAdapter( this, R.layout.list_reservation_conference, data2);
        Reservation_mem_adapter = new List_Reservation_MemAdapter( this, R.layout.list_reservation_member, data1);

        printReservation_list();
        join_conference_layout = (LinearLayout) findViewById(R.id.join_conference_layout);
        join_conference_text = (TextView) findViewById(R.id.join_conference_text);
        join_conference_gohome = (ImageButton) findViewById(R.id.join_conference_gohome);
        join_conference_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Join_Meeting.this,MainActivity.class);
                startActivity(intent);
            }
        });
        reservation_conference_list.setAdapter(Reservation_adapter);
        reservation_conference_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                printReservation_mem_list(position);
                choice_mid = data2.get(position).getMid();
                choice_made = data2.get(position).getMade();
                choice_mname = data2.get(position).getMname();
                choice_mwhere = data2.get(position).getWhere();
                choice_syear = data2.get(position).getAlldate().substring(0,4);
                choice_smon = data2.get(position).getAlldate().substring(5,7);
                choice_sday = data2.get(position).getAlldate().substring(8,10);

                Log.d("초이스미드", choice_mid);
            }
        });

        smon = s.format(new Date());
        sday = s1.format(new Date());
        sdow = s2.format(new Date());
        syear = s3.format(new Date());

        reservation_con_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("레졀베이션 스타트","왓");
                startReservationMeeting();
                Intent intent = new Intent(Join_Meeting.this,Conference.class);
                intent.putExtra("mname",choice_mname);
                intent.putExtra("where",choice_mwhere);
                intent.putExtra("syear",choice_syear);
                intent.putExtra("smon",choice_smon);
                intent.putExtra("sday",choice_sday);
                intent.putExtra("choice_mid",choice_mid);
                intent.putExtra("flag",true);
                startActivity(intent);
//                Intent intent = new Intent(Join_Meeting.this, Conference.class);
//                intent.putExtra("mname", mname.get(con_position));
//                intent.putExtra("madename", made.get(con_position));
//                intent.putExtra("where", where.get(con_position));
//                intent.putExtra("mid", mid.get(con_position));
//                intent.putExtra("flag", true);
//                intent.putExtra("smon", smon);
//                intent.putExtra("sday", sday);
//                intent.putExtra("sdow", sdow);
//                intent.putExtra("smon", smon);
//                intent.putExtra("syear",syear);
//                startActivity(intent);
            }
        });
    }
    private void printReservation_list(){
        Cookie cookie = (Cookie) getApplication();
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/info?unum="+cookie.getCookie()+"&done="+false;
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
                                Log.e("예약된 회의 불러오기 실패", response.getString("message"));
                                //Toast.makeText(root.getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("예약된 회의 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                Log.d("데이터"," " +jsonArray.toString());
                                for(int i =0; i<jsonArray.length();i++){
                                    JSONArray member_raw = new JSONArray(jsonArray.getJSONObject(i).getJSONArray("member_raw").toString());
                                    for(int j=0;j<member_raw.length();j++) {
                                        Log.d("으",""+member_raw.getInt(j));
                                    }

                                    mid.add(jsonArray.getJSONObject(i).getString("_id"));
                                    Log.d("mid",mid.get(i));
                                    mname.add(jsonArray.getJSONObject(i).getString("mname"));
                                    Log.d("mname",mname.get(0));
                                    date.add(jsonArray.getJSONObject(i).getString("date"));
                                    Log.d("date",date.get(0));
                                    where.add(jsonArray.getJSONObject(i).getString("where"));
                                    Log.d("where",where.get(0));
                                    made.add(jsonArray.getJSONObject(i).getJSONObject("made").getString("uname"));
                                    Log.d("made",made.get(0));
                                    memo.add(jsonArray.getJSONObject(i).getString("memo"));
                                    Log.d("memo",memo.get(0));
                                    start.add(jsonArray.getJSONObject(i).getString("start"));
                                    Log.d("start",start.get(0));
                                    __v.add(jsonArray.getJSONObject(i).getInt("__v"));
                                    Log.d("__v"," "+__v.get(0));
                                }
                                Log.d("time",date.get(0).substring(11,16));
                                Log.d("madeSize"," "+made.size());
                                for(int i=0;i<made.size();i++){
                                    Log.d("madename",made.get(i));
                                    time.add(date.get(i).substring(11,16));
                                    list_m_item = new List_m_Item(mid.get(i),made.get(i),mname.get(i),time.get(i),where.get(i),date.get(i).substring(5,10),date.get(i));
                                    data2.add(list_m_item);
                                    Reservation_adapter.notifyDataSetChanged();
                                }

                                //Toast.makeText(root.getApplicationContext(), "진행중인 회의 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
                            //Toast.makeText(root.getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("예약된 회의 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("예약된 회의 불러오기 실패", error.toString());
                        //Toast.makeText(root.getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }

//    private void printReservation_mem_list(int position){
//        this.con_position = position;
//        Cookie cookie = (Cookie) root.getApplication();
//        final RequestQueue queue = Volley.newRequestQueue(view.getContext());
//        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/info?unum="+cookie.getCookie()+"&done="+false;
//        Log.i("url", url);
//
//        // 일단 시작
//        queue.start();
//
//        // 요청 만들기
//        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    // 제대로 된 응답이 들어왔을 시
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (response.has("message")) { // 메세지 가지고 있음 에러난거
//                                // 오류를 사용자에게 알리도록 고칠 것.
//                                // message에 있는 내용을 알릴 것.
//                                Log.e("예약된 회의 멤버 불러오기 실패", response.getString("message"));
//                                //Toast.makeText(root.getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
//                            } else { // 메세지 없으면 성공한거
//                                Log.v("예약된 회의 멤버 불러오기 성공", "success");
//                                Log.i("response", response.toString());
//                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
//                                Log.d("데이터"," " +jsonArray.toString());
//
//                                JSONArray member_raw = new JSONArray(jsonArray.getJSONObject(con_position).getJSONArray("member_raw").toString());
//                                for(int j=0;j<member_raw.length();j++) {
//                                    Log.d("으",""+member_raw.getInt(j));
//                                    member.add(member_raw.getInt(j));
//                                    Log.d("제대로 들어갔나"," "+member.get(j));
//                                    JSONObject member_info =new JSONObject(jsonArray.getJSONObject(con_position).get("member").toString());
//                                    mem_uname.add(member_info.getJSONObject(member.get(j).toString()).getString("uname"));
//                                    Log.d("멤네"," "+mem_uname.get(j));
//                                    mem_company.add(member_info.getJSONObject(member.get(j).toString()).getString("company"));
//                                    Log.d("멤회"," "+mem_company.get(j));
//                                    mem_department.add(member_info.getJSONObject(member.get(j).toString()).getString("department"));
//                                    Log.d("멤디"," "+mem_department.get(j));
//
//                                    list_item = new List_Item(mem_uname.get(j),mem_company.get(j),mem_department.get(j));
//                                    data1.add(list_item);
//                                    Reservation_mem_adapter.notifyDataSetChanged();
//                                }
//
//
//                                //Toast.makeText(root.getApplicationContext(), " 예약된 회의 멤버 불러오기 성공", //Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            // 오류를 사용자에게 알리도록 고칠 것.
//                            // 알 수 없는 원인.
//                            // e.printStackTrace();
//                            //Toast.makeText(root.getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
//                    public void onErrorResponse(VolleyError error) {
//                        // 오류를 사용자에게 알리도록 고칠 것.
//                        Log.e("예약된 회의 멤버 불러오기 실패", "인터넷 연결을 확인해주세요.");
//                        Log.e("예약된 회의 멤버 불러오기 실패", error.toString());
//                        //Toast.makeText(root.getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
//                    }
//                });
//        // 요청 큐에 추가하기
//        queue.add(jsonObjectRequest);
//
//
//    }

    private void startReservationMeeting(){

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/start?mid="+choice_mid;
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
                                Log.e("예약된 회의 시작 실패", response.getString("message"));
                                //Toast.makeText(root.getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("예약된 회의 시작 성공", "success");
                                Log.i("response", response.toString());
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                Log.d("데이터"," " +jsonArray.toString());

                                //Toast.makeText(root.getApplicationContext(), " 예약된 회의 멤버 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
                            //Toast.makeText(root.getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("예약된 회의 시작 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("예약된 회의 시작 실패", error.toString());
                        //Toast.makeText(root.getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }

}


