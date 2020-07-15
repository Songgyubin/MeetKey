package com.naver.naverspeech.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;


public class Joinadd_Search extends AppCompatActivity {



    private TextInputEditText user_id;
    private TextView user_name;
    private TextView user_company;
    private TextView user_department;
    private TextView user_position;
    private ImageView user_image;

    private int fnum;
    private String userid;
    private String username;
    private String usercompany;
    private String userdepartment;
    private String userposition;

    private Button joinadd;
    private ImageButton joinadd_search_gohome;

    JSONObject favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinadd_search);

        user_id = (TextInputEditText) findViewById(R.id.user_id);
        user_name = (TextView) findViewById(R.id.user_name);
        user_company = (TextView) findViewById(R.id.user_company);
        user_department = (TextView) findViewById(R.id.user_department);
        user_position = (TextView) findViewById(R.id.user_position);
        user_image = (ImageView) findViewById(R.id.user_image);

        joinadd = (Button) findViewById(R.id.joinadd);
        joinadd.setOnClickListener(joinaddEvent);
        joinadd_search_gohome = (ImageButton) findViewById(R.id.joinadd_search_gohome);
        joinadd_search_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Joinadd_Search.this,MainActivity.class);
                startActivity(intent);
            }
        });

        user_id.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        //EditText 끝에 x표시 기능  android:drawableRight="@drawable/delete_text"
//        user_id.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWBLE_RIGHT = 2;
//
//                if(event.getAction() == MotionEvent.ACTION_UP){
//                    if(event.getRawX() >= (user_id.getRight() - user_id.getCompoundDrawables()[DRAWBLE_RIGHT].getBounds().width()))
//                        user_id.setText("");
//                        user_id.setHint("ID");
//                        return true;
//                }
//
//                return false;
//            }
//        });
        user_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(EditorInfo.IME_ACTION_SEARCH ==actionId){

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("uid", user_id.getText().toString());

                    final RequestQueue queue = Volley.newRequestQueue(Joinadd_Search.this);
                    final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/search";

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
                                            Log.e("검색 실패", response.getString("message"));
                                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                        } else { // 메세지 없으면 성공한거
                                            Log.v("검색 성공", "success");
                                            // //Toast.makeText(getApplicationContext(),"검색 성공", //Toast.LENGTH_LONG).show();

                                            favorite = response;
                                            fnum = Integer.parseInt(response.get("unum").toString());
                                            user_name.setText(response.get("uname").toString());
                                            user_company.setText(response.get("company").toString());
                                            user_department.setText(response.get("department").toString());
                                            user_position.setText(response.get("position").toString());
                                            user_image.setImageResource(R.drawable.account48);

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
                                    Log.e("검색 실패", "인터넷 연결을 확인해주세요.");
                                    //Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                                }
                            });
                    // 요청 큐에 추가하기
                    queue.add(jsonObjectRequest);

                }
            else{
                return false;}
                return true;
            }
        });

    }

    // 검색 버튼에 붙은 클릭 이벤트


    Button.OnClickListener joinaddEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                Cookie cookie = (Cookie) getApplication();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("unum", String.valueOf(cookie.getCookie()));
                params.put("fnum", favorite.get("unum").toString());

                final RequestQueue queue = Volley.newRequestQueue(Joinadd_Search.this);
                final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/favorite";

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
                                        Log.e("추가 실패", response.getString("message"));
                                        //Toast.makeText(getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
                                    } else { // 메세지 없으면 성공한거
                                        Log.v("추가 성공", "success");
                                        //Toast.makeText(getApplicationContext(), "추가 성공", //Toast.LENGTH_LONG).show();
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
                                Log.e("추가 실패", "인터넷 연결을 확인해주세요.");
                                //Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                            }
                        });
                // 요청 큐에 추가하기
                queue.add(jsonObjectRequest);

                Intent intent = new Intent(Joinadd_Search.this, MainActivity.class);
                intent.putExtra("친구추가","친구추가");
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
            }
        };


    }

