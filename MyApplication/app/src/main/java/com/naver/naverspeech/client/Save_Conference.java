package com.naver.naverspeech.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class Save_Conference extends AppCompatActivity {


    private Button btn_save;
    private Button camera_upload;
    private TextView save_subject;
    private TextView save_room;
    private TextInputEditText num_cluster;
    private TextInputLayout num_cluster_layout;


    private String string_subject;
    private String string_room;
    private String string_year;
    private String string_month;
    private String string_day;
    private String string_dow;
    private String string_time;
    private int save_num_cluster =0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_conference);
        context = this;
        btn_save = (Button) findViewById(R.id.btn_save);
        camera_upload = (Button) findViewById(R.id.camera_upload);
        save_subject = (TextView) findViewById(R.id.save_subject);
        save_room = (TextView) findViewById(R.id.save_room);
        num_cluster = (TextInputEditText) findViewById(R.id.num_cluster);
        num_cluster_layout = (TextInputLayout) findViewById(R.id.num_cluster_layout);

        Intent intent = new Intent(this.getIntent());
        string_subject =  intent.getStringExtra("subject");
        string_room = intent.getStringExtra("meeting_room");
        string_year = intent.getStringExtra("year");
        string_month = intent.getStringExtra("month");
        string_day = intent.getStringExtra("day");
        string_dow = intent.getStringExtra("dow");
        string_time = intent.getStringExtra("time");

        save_subject.setText(string_subject);
        save_room.setText(string_room);
        num_cluster.addTextChangedListener(num_Cluster);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_num_cluster = Integer.parseInt(num_cluster.getText().toString());
                Cookie cookie = (Cookie) getApplication();
                final RequestQueue queue = Volley.newRequestQueue(context);
                final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/end?num_cluster="+save_num_cluster+"&mid="+cookie.getMid();
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
                                        Log.e("회의 저장 실패", response.getString("message"));
//                                        //Toast.makeText(getApplicationContext(),response.getString("message"), //Toast.LENGTH_LONG).show();
                                    } else { // 메세지 없으면 성공한거
                                        Log.v("회의 저장 성공", "success");
                                        Log.i("response", response.toString());

//                                        //Toast.makeText(getApplicationContext(),"회의 저장 성공", //Toast.LENGTH_LONG).show();
                                    }
                                } catch(JSONException e){
                                    // 오류를 사용자에게 알리도록 고칠 것.
                                    // 알 수 없는 원인.
                                    // e.printStackTrace();
//                                    //Toast.makeText(getApplicationContext(),"알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                            public void onErrorResponse(VolleyError error) {
                                // 오류를 사용자에게 알리도록 고칠 것.
                                Log.e("회의 저장 실패", "인터넷 연결을 확인해주세요.");
                                Log.e("회의 저장 실패", error.toString());
//                                //Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                            }
                        });
                // 요청 큐에 추가하기
                queue.add(jsonObjectRequest);
                Intent intent1 = new Intent(Save_Conference.this,MainActivity.class);
                startActivity(intent1);
            }
        });

    }
    TextWatcher num_Cluster = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
//            save_num_cluster = Integer.parseInt(num_cluster.getText().toString());
            String check;
            check= num_cluster.getText().toString();
            for(int i =0;i<check.length();i++) {
                if (check.charAt(i)=='개') {
                    num_cluster_layout.setError("숫자만 입력해주세요");
                }
                else {
                    num_cluster_layout.setError(null);
                }
            }
        }
    };

    public void onClick (View v){
        switch (v.getId()){
            case R.id.camera_upload:
               startActivity(new Intent(Save_Conference.this, Camera_popup_conference.class));
                break;
        }
    }



}
