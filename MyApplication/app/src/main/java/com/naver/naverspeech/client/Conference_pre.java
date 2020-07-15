package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import static com.naver.naverspeech.client.MainActivity.viewPager;


public class Conference_pre extends AppCompatActivity {

    private TextInputEditText subject;
    private TextInputEditText meeting_room;
    private TextInputEditText note;
    private TextInputLayout textInputLayout;
    private Button add_invite;
    private Button start_meeting;
    private TextView month;
    private TextView day;
    private TextView dow;
    private ListView invited_user_list;
    private ArrayList<List_Item> data = new ArrayList<>();
    private List_friendsAdapter adapter;
    private List_Item list_item;
    private ImageButton cpre_gohome;

    private ArrayList<Integer> invited_fnums = new ArrayList<Integer>();
    private ArrayList<String> invited_fnames = new ArrayList<String>();
    private ArrayList<String> invited_fcompanys = new ArrayList<String>();
    private ArrayList<String> invited_fdepartments = new ArrayList<String>();
    private ArrayList<String> invited_fpositions = new ArrayList<String>();
    private ArrayList<Drawable> invited_f_drawbles = new ArrayList<Drawable>();
    private Cookie cookie;
    private String syear;
    private String smon;
    private String sday;
    private String sdow;
    private String stime;
    private String save_subject=null;
    private String save_meeting_room=null;
    private String save_note = null;
    private String mid;
    private boolean invite_flag = true;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference_pre);
        cookie = (Cookie) getApplication();
        intent = new Intent(Conference_pre.this, Conference.class);
//        Intent intent1 = root.getIntent();
//        f= intent1.getBooleanExtra("con",false);
//        if(f) {
//            TabPagerAdapter pagerAdapter1 = new TabPagerAdapter(getFragmentManager(), 2);
//            viewPager.setAdapter(pagerAdapter1);
//        }else{
//            System.out.println("하하하");
//        }

        cpre_gohome = (ImageButton) findViewById(R.id.cpre_gohome);
        cpre_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conference_pre.this,MainActivity.class);
                startActivity(intent);
            }
        });
        invited_user_list = (ListView) findViewById(R.id.invited_user_list);
        adapter = new List_friendsAdapter(this, R.layout.list_friends, data);
        invite_flag = printInvitedUser();
        invited_user_list.setAdapter(adapter);
        month = (TextView) findViewById(R.id.conference_pre_month);
        day = (TextView) findViewById(R.id.conference_pre_day);
        dow = (TextView) findViewById(R.id.conference_pre_dow);
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("dd");
        SimpleDateFormat s2 = new SimpleDateFormat("EE");
        SimpleDateFormat s3 = new SimpleDateFormat("yyyy");
        SimpleDateFormat s4 = new SimpleDateFormat("hhmmss");
        smon = s.format(new Date());
        sday = s1.format(new Date());
        sdow = s2.format(new Date());
        syear = s3.format(new Date());
        stime = s4.format(new Date());

        if (smon.length() == 1) {
            final SpannableStringBuilder sps = new SpannableStringBuilder(smon+"월");
            sps.setSpan(new AbsoluteSizeSpan(60), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            month.setText(sps);
        } else {
            final SpannableStringBuilder sps = new SpannableStringBuilder(smon+"월");
            sps.setSpan(new AbsoluteSizeSpan(60), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            month.setText(sps);
        }
        if (sday.length() == 1) {
            final SpannableStringBuilder sps = new SpannableStringBuilder(sday+"일");
            sps.setSpan(new AbsoluteSizeSpan(60), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            day.setText(sps);
        } else {
            final SpannableStringBuilder sps = new SpannableStringBuilder(sday+"일");
            sps.setSpan(new AbsoluteSizeSpan(60), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            day.setText(sps);
        }

            final SpannableStringBuilder sps = new SpannableStringBuilder(sdow);
            sps.setSpan(new AbsoluteSizeSpan(60), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dow.setText(sps);

//        month.setText(smon);
//        day.setText(sday);
//        dow.setText(sdow);
        Log.d("년도는",syear);
        Log.d("회의시작시간",stime);

        note = (TextInputEditText) findViewById(R.id.note);
        subject = (TextInputEditText) findViewById(R.id.subject);
        meeting_room = (TextInputEditText) findViewById(R.id.meeting_room);
        add_invite = (Button) findViewById(R.id.add_invite);
        add_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_subject = subject.getText().toString();
                save_meeting_room = meeting_room.getText().toString();
                save_note = note.getText().toString();

                if (save_subject.isEmpty() || save_meeting_room.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "회의주제와 회의실을 입력해주세요",Toast.LENGTH_LONG).show();

                }
                else {
                    Intent intent = new Intent(Conference_pre.this, Invite_User.class);
                    intent.putExtra("subject",save_subject);
                    intent.putExtra("room",save_meeting_room);
                    intent.putExtra("note",save_note);
                    startActivity(intent);
                }
            }
        });
        start_meeting = (Button) findViewById(R.id.start_meeting);
        start_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save_subject = subject.getText().toString();
                save_meeting_room = meeting_room.getText().toString();

                if (save_subject.isEmpty() || save_meeting_room.isEmpty()) {
                    //Toast.makeText(root.getApplication(), "회의주제와 회의실을 입력해주세요", //Toast.LENGTH_LONG).show();
                }
                else if(adapter.getCount()==0){
                    //Toast.makeText(root.getApplication(),"초대자를 선택해주세요",//Toast.LENGTH_LONG).show();
                }
                else {

                    intent.putExtra("subject", save_subject);
                    intent.putExtra("meeting_room", save_meeting_room);
                    intent.putExtra("note",save_note);
                    intent.putExtra("year", syear);
                    intent.putExtra("month", smon);
                    intent.putExtra("day", sday);
                    intent.putExtra("dow", sdow);
                    intent.putExtra("time", stime);
                    intent.putExtra("con_pre",true);
                    intent.putExtra("invited_fnums",invited_fnums);
                    startActivity(intent);
                    startMeeting();
                }
            }
        });
        if(invite_flag){
            Intent intent = getIntent();
            subject.setText(intent.getStringExtra("subject"));
            meeting_room.setText(intent.getStringExtra("room"));
            note.setText(intent.getStringExtra("note"));

        }

    }

    private boolean printInvitedUser() {
        Intent intent = getIntent();

        invited_fnums = intent.getIntegerArrayListExtra("fnums");
        invited_fnames = intent.getStringArrayListExtra("fnames");
        invited_fcompanys = intent.getStringArrayListExtra("fcompanys");
        invited_fdepartments = intent.getStringArrayListExtra("fdepartments");
        invited_fpositions = intent.getStringArrayListExtra("fpositions");

        try {
//            Thread.sleep(300);
            for (int i = 0; i < invited_fnums.size(); i++) {

                list_item = new List_Item(invited_fnames.get(i), invited_fcompanys.get(i), invited_fdepartments.get(i), invited_fnums.get(i),invited_fpositions.get(i));
                data.add(list_item);
                adapter.notifyDataSetChanged();
            }
            Log.d("invited_fnum",""+invited_fnums);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("초대유저","왜두번");
        return true;
    }
    private void startMeeting() {
        HashMap<String,Object> params = new HashMap<>();
        params.put("mname",save_subject);
        params.put("date",syear+"-"+smon+"-"+sday);
        params.put("time",stime.substring(0,2)+":"+stime.substring(2,4)+":"+stime.substring(4));
        params.put("where",save_meeting_room);
        params.put("made",cookie.getCookie());
        params.put("memo",save_note);
        params.put("member",invited_fnums);

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/info";
        Log.i("url", url);

        // 일단 시작
        queue.start();

        // 요청 만들기
        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    // 제대로 된 응답이 들어왔을 시
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.has("message")) { // 메세지 가지고 있음 에러난거
                                // 오류를 사용자에게 알리도록 고칠 것.
                                // message에 있는 내용을 알릴 것.
                                Log.e("회의생성 실패", response.getString("message"));
//                                //Toast.makeText(root,response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("회의생성 성공", "success");
                                Log.i("response", response.toString());
                                Log.d("보낼꺼 mid",response.getString("_id"));
                                Log.d("실행순서","startmeeting");
                                mid = response.getString("_id");
                                cookie.setMid(mid);

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
                        Log.e("회의생성 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("회의생성 실패", error.toString());
//                        //Toast.makeText(root,"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }
}

