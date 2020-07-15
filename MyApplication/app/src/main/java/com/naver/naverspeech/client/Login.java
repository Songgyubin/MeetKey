package com.naver.naverspeech.client;


import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.Chart;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    private TextInputEditText userid;
    private TextInputEditText userpw;
    private Button login;
    private TextView signup;
    private TextView forget_password;
    private Cookie cookie;
    private String id;
    private String pw;
    private String uname;
    private String ucompany;
    private String udepartment;
    private String uposition;
    private Drawable uImage;
    private Context context;
    private ArrayList<MyData> myDataset;
    private ArrayList<String> mid = new ArrayList<>();
    private ArrayList<String> con_date = new ArrayList<>();
    private ArrayList<String> con_summarize = new ArrayList<>();
    private ArrayList<String> con_made = new ArrayList<>();
    private ArrayList<ArrayList<String>> focus_summarize = new ArrayList<>();
    private String madename = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = this;
        cookie = (Cookie) getApplication();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        userid = (TextInputEditText) findViewById(R.id.userid);
        userpw = (TextInputEditText) findViewById(R.id.userpw);
        login = (Button) findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.signup);
        login.setOnClickListener(loginEvent);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Login.this, Sign_Up.class);
                startActivity(intent1);
            }
        });
    }

    // 로그인 버튼에 붙은 클릭 이벤트
    Button.OnClickListener loginEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            login();
        }
    };

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.signup:
                Intent intent = new Intent(Login.this, Sign_Up.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            //Toast.makeText(getApplicationContext(), "한번 더 누르시면 앱이 종료됩니다", //Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", userid.getText().toString());
        params.put("pw", userpw.getText().toString());
        final RequestQueue queue1 = Volley.newRequestQueue(Login.this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/login";

        // 일단 시작
        queue1.start();

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
                                Log.e("로그인 실패", response.getString("message"));
//                                    //Toast.makeText(getApplicationContext(),response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("로그인 성공", "success");
                                Log.i("response", response.toString());

                                cookie.setCookie(Integer.parseInt(response.getString("unum")));
                                uImage = context.getResources().getDrawable(R.drawable.account1);

                                Log.i("cookie", String.valueOf(cookie.getCookie()));

                                uname = response.getString("uname");
                                ucompany = response.getString("company");
                                udepartment = response.getString("department");
                                uposition = response.getString("position");

                                cookie.setUserInfo(uname, ucompany, udepartment, uposition, uImage);
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("myID", userid.getText().toString());
//                                    //Toast.makeText(getApplicationContext(),"로그인 성공", //Toast.LENGTH_LONG).show();
                                startActivity(intent); // 메인으로 넘어가자

                            }
                        } catch (JSONException e) {
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
                        Log.e("로그인 실패", "인터넷 연결을 확인해주세요.");
                        //Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue1.add(jsonObjectRequest);

    }

//    private void History_getMid(){
//
//        final Cookie cookie = (Cookie) getApplication();
//
//        final RequestQueue queue = Volley.newRequestQueue(this);
//        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/info?unum="+cookie.getCookie()+"&done="+true;
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
////                                //Toast.makeText(root.getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
//                            } else { // 메세지 없으면 성공한거
//                                Log.v("예약된 회의 멤버 불러오기 성공", "success");
//                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
//                                JSONArray jsonArray1 = new JSONArray(response.getJSONArray("summarize").toString());
//                                Log.d("모든 주제"," " +jsonArray1);
//                                Log.d("모든 주제 개수"," "+jsonArray1.length());
//                                Log.d("로그인에 있는 getMid","되냐");
//                                for(int i=0;i<jsonArray1.length();i++){
//                                    for(int j=0;j<jsonArray1.getJSONArray(i).length();j++){
//                                        con_summarize.add(jsonArray1.getJSONArray(i).getString(j));
//                                    }
//                                    focus_summarize.add(con_summarize);
//                                }
//                                Log.d("핵심주제",focus_summarize.get(0).get(0));
//                                Log.d("핵심주제",focus_summarize.get(0).get(1));
//                                Log.d("주제1",jsonArray1.getJSONArray(0).toString());
//                                Log.d("주제2",jsonArray1.getJSONArray(1).toString());
//                                Log.d("주제1-1",jsonArray.getJSONArray(1).getString(0));
//                                Log.d("주제1-2",jsonArray.getJSONArray(1).getString(1));
//                                Log.d("made",""+response.getJSONObject("made"));
//                                Log.d("made1",""+response.getJSONObject("made").getString("uname"));
//                                con_made.add(response.getJSONObject("made").getString("uname"));
//                                Log.d("date",response.getString("date").substring(0,10));
//                                con_date.add(response.getString("date").substring(0,10));
//                                Log.d("라라",con_made.get(0)+" "+con_date.get(0));
//                                myDataset.add(new MyData("2018-09-05","인공지능 회의 분석 서비스 ","오정현",R.drawable.history_image1));
//
//
//                            }
//                        } catch (JSONException e) {
//                            // 오류를 사용자에게 알리도록 고칠 것.
//                            // 알 수 없는 원인.
//                            // e.printStackTrace();
////                            //Toast.makeText(root.getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
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
////                        //Toast.makeText(root.getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
//                    }
//                });
//        // 요청 큐에 추가하기
//        queue.add(jsonObjectRequest);
//
//    }
//
//
//
}