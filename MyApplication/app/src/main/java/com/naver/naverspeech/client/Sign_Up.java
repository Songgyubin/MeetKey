package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;

public class Sign_Up extends Activity {

    ImageView sign_image;
    EditText sign_id; //id
    EditText sign_pw; //password
    EditText sign_name; //username
    EditText sign_company; //comname
    EditText sign_department; //department
    EditText sign_position; //position

    Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Intent intent1 = getIntent();
        if (intent1.getStringExtra("name") != null || intent1.getStringExtra("id") != null
                || intent1.getStringExtra("pw") != null || intent1.getStringExtra("company") != null
                || intent1.getStringExtra("position") != null || intent1.getStringExtra("department") != null) {
            sign_name.setText(intent1.getStringExtra("name"));
            sign_id.setText(intent1.getStringExtra("id"));
            sign_pw.setText(intent1.getStringExtra("pw"));
            sign_company.setText(intent1.getStringExtra("company"));
            sign_department.setText(intent1.getStringExtra("department"));
            sign_position.setText(intent1.getStringExtra("position"));
        }

        sign_image = (ImageView) findViewById(R.id.sign_image);
        sign_id = (EditText) findViewById(R.id.sign_id);
        sign_pw = (EditText) findViewById(R.id.sign_password);
        sign_name = (EditText) findViewById(R.id.sign_name);
        sign_company = (EditText) findViewById(R.id.sign_company);
        sign_department = (EditText) findViewById(R.id.sign_department);
        sign_position = (EditText) findViewById(R.id.sign_position);


        join = (Button) findViewById(R.id.join);
        join.setOnClickListener(joinEvent);
        sign_image.setOnClickListener(new View.OnClickListener() {
            String name;
            String id;
            String pw;
            String company;
            String department;
            String position;
            @Override
            public void onClick(View v) {


                if (sign_name.getText().toString() != null|| sign_id.getText().toString() != null|| sign_pw.getText().toString() != null
                        || sign_company.getText().toString() != null|| sign_department.getText().toString() != null|| sign_position.getText().toString() != null
                ) {
                    name = sign_name.getText().toString();
                    id = sign_id.getText().toString();
                    pw = sign_pw.getText().toString();
                    company = sign_company.getText().toString();
                    department = sign_department.getText().toString();
                    position = sign_position.getText().toString();

                }

                Intent intent = new Intent(Sign_Up.this, Camera_popup_conference.class);
                if (name != null || id != null || pw != null || company != null || department != null || position != null) {
                    intent.putExtra("name", name);
                    intent.putExtra("id", id);
                    intent.putExtra("pw", pw);
                    intent.putExtra("company", company);
                    intent.putExtra("department", department);
                    intent.putExtra("position", position);
                    startActivity(intent);
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    Button.OnClickListener joinEvent = new View.OnClickListener() {
        // join에 붙은 클릭 이벤트 리스너
        public void onClick(View view) {

            // 데이터 생성
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("uid", sign_id.getText().toString());
            params.put("pw", sign_pw.getText().toString());
            params.put("uname", sign_name.getText().toString());
            params.put("company", sign_company.getText().toString());
            params.put("department", sign_department.getText().toString());
            params.put("position", sign_position.getText().toString());

            // volley 패키지를 이용하여 통신
            final RequestQueue queue = Volley.newRequestQueue(Sign_Up.this);
            final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/register";

            // 일단 스타트
            queue.start();

            // 요청 만들기
            // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        // 제대로 된 응답이 들어왔을 시
                        public void onResponse(JSONObject response) {
                            try {
                                // message를 가지고 있으면 에러난 거
                                if (response.has("message")) {
                                    // 오류를 사용자에게 알리도록 고칠 것.
                                    // message에 있는 내용을 알릴 것.
                                    Log.e("회원가입 실패", response.getString("message"));
                                    //Toast.makeText(getApplicationContext(),response.getString("message"),//Toast.LENGTH_LONG).show();
                                } else { // message 안가지고 있으면 user 정보 통째로 건네줌
                                    // 성공하면 어디로 보낼까
                                    Log.v("회원가입 성공", "success");
                                    //Toast.makeText(getApplicationContext(),"회원가입성공",//Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                // 오류를 사용자에게 알리도록 고칠 것.
                                // 알 수 없는 원인.
                                // e.printStackTrace();
                                Log.e("error", e.toString());
                                //Toast.makeText(getApplicationContext(),"알수없는 이유로 실패",//Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                        public void onErrorResponse(VolleyError error) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            Log.e("회원가입 실패", "알 수 없는 이유로 실패");
                            //Toast.makeText(getApplicationContext(),"알수없는 이유로 실패",//Toast.LENGTH_LONG).show();
                        }
                    });

            // 만들어논 요청을 큐에 추가함
            queue.add(jsonObjectRequest);
        }
    };


}