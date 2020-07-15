package com.naver.naverspeech.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    private TabLayout tabLayout;
    static ViewPager viewPager;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ImageView accout_image;
    private TextView myID;
    private Button navi_gohome;
    private Button navi_myinfo;
    private Button navi_setting;
    private View c_pre_view;
    private TextInputEditText pre_subject;
    private TextInputEditText pre_meeting_room;
    private LinearLayout main_content_layout;
    private Button main_join_meeting;
    private Button main_start_meeting;
    private Button main_add_friend;
    private TextView num_of_conference;

    private String navi_myID;
    private String navi_mycompany;
    private String navi_mydepartment;
    private String navi_myposition;
    private boolean navi_flag = false;
    private int tab_position = 0;
    private boolean view_flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        navi_myID = intent.getStringExtra("myID");
        navi_mycompany = intent.getStringExtra("mycompany");
        navi_mydepartment = intent.getStringExtra("mydepartment");
        navi_myposition = intent.getStringExtra("myposition");
        myID = (TextView) findViewById(R.id.navi_myID);
        myID.setText(navi_myID);
        navi_gohome = (Button)findViewById(R.id.navi_gohome);
        main_content_layout = (LinearLayout) findViewById(R.id.main_content_layout);
        //Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        accout_image = (ImageView) findViewById(R.id.account_image);
        main_join_meeting = (Button) findViewById(R.id.main_join_meeting);
        main_add_friend = (Button) findViewById(R.id.main_add_friend);
        main_start_meeting = (Button) findViewById(R.id.main_start_meeting);
        num_of_conference = (TextView) findViewById(R.id.num_of_conference);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_white);

        //Initializing the TabLayout;
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

//        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(pagerAdapter);
        view_flag = getIntent().getBooleanExtra("con",true);
        //Creating adapter

        if(view_flag==true) {
            tabLayout.addTab(tabLayout.newTab().setText("회의록"));
            tabLayout.addTab(tabLayout.newTab().setText("계정&그룹"));
            tabLayout.addTab(tabLayout.newTab().setText("회의예약"));
//            tabLayout.addTab(tabLayout.newTab().setText("회의시작"));
//            tabLayout.addTab(tabLayout.newTab().setText("회의참가"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pagerAdapter);
        }else if (view_flag==false){
            tabLayout.addTab(tabLayout.newTab().setText("회의록"));
            tabLayout.addTab(tabLayout.newTab().setText("계정&그룹"));
            tabLayout.addTab(tabLayout.newTab().setText("회의예약"));
//            tabLayout.addTab(tabLayout.newTab().setText("회의시작"));
//            tabLayout.addTab(tabLayout.newTab().setText("회의참가"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addTab(tabLayout.newTab().setText("회의록"),0);
            tabLayout.addTab(tabLayout.newTab().setText("계정&그룹"),1);
            tabLayout.addTab(tabLayout.newTab().setText("회의예약"),2);
//            tabLayout.addTab(tabLayout.newTab().setText("회의시작"),2);
//            tabLayout.addTab(tabLayout.newTab().setText("회의참가"),3);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//            tabLayout.removeTabAt(4);
//            tabLayout.removeTabAt(5);
//            tabLayout.removeTabAt(4);
//            tabLayout.removeTabAt(4);
//            viewPager.setCurrentItem(2);


//            tabLayout.removeTabAt(4);
//            tabLayout.removeTabAt(5);
//            tabLayout.removeTabAt(6);
//            tabLayout.removeTabAt(7);
// 질문: removeTabAt(5)까지는 잘 지워지는데 6부터는 null포인트에러가남 getTabCount()쓰면 8개인데
//            6 7은 왜 널포인트가 뜨고 위에 코드 처럼 4 5 4 4 이렇게하면 지워지는지....
            c_pre_view = getLayoutInflater().inflate(R.layout.conference_pre,null,false);
            pre_subject = (TextInputEditText) c_pre_view.findViewById(R.id.subject);
            pre_meeting_room = (TextInputEditText) c_pre_view.findViewById(R.id.meeting_room);
            pre_subject.setText(getIntent().getStringExtra("subject"));
            pre_meeting_room.setText(getIntent().getStringExtra("room"));

        }
        printReservation_list();
        //탭 페이지 바뀔때 해당 탭 표시
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("이것은", "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("이것은", "onPageSelected: " + position);
                tab_position = position;
                setSupportActionBar(toolbar);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("이것은", "onPageScrollStateChagned: " + state);
            }
        });
        //Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
                if (tab.getPosition() != 0) {
                    main_content_layout.setVisibility(View.GONE);
                } else {
                    main_content_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    // 액션바 옵션아이콘 등
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d("요거는", "onCreateOptionsMenu");

        switch (tab_position) {
            case 0:
                getMenuInflater().inflate(R.menu.toolbar_history_feed, menu);
                invalidateOptionsMenu();
                break;
            case 1:
                getMenuInflater().inflate(R.menu.toolbar_join_add, menu);
                invalidateOptionsMenu();
                break;

        }
        return true;
    }

    // 액션바 아이콘 클릭이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history_search:
                // TODO : process the click event for action_search item.
                return true;
            case R.id.action_add_friend: {
                Intent intent = new Intent(MainActivity.this, Joinadd_Search.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_add_group: {
                Intent intent = new Intent(MainActivity.this, Make_Groups.class);
                startActivity(intent);
                return true;
            }
//            case R.id.action_conference_reservation: {
//                Intent intent = new Intent(MainActivity.this, Conference_Reservation.class);
//                startActivity(intent);
//                return true;
//            }
            case R.id.action_edit_friend: {
                Intent intent = new Intent (MainActivity.this, Join_Edit.class);
                startActivity(intent);
                return true;
            }


            case android.R.id.home:
                //Toast.makeText(getApplicationContext(), "네비게이션바", //Toast.LENGTH_LONG).show();
//                listView.setAdapter(adapter);
//                toolbar.hideOverflowMenu();
//                drawer.isDrawerVisible(Gravity.LEFT);

                drawer.openDrawer(GravityCompat.START);
                navi_flag = true;

                return true;
            // ...
            // ...
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (navi_flag == true) {
            drawer.closeDrawer(GravityCompat.START);
            navi_flag = false;
        } else
            super.onBackPressed();

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            //Toast.makeText(getApplicationContext(), "한번 더 누르시면 앱이 종료됩니다", //Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.navi_gohome:
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            break;

            case R.id.main_add_friend:
                Intent intent1 = new Intent(MainActivity.this,Joinadd_Search.class);
                startActivity(intent1);
                break;

            case R.id.main_join_meeting:
                Intent intent2 = new Intent(MainActivity.this,Join_Meeting.class);
                startActivity(intent2);
                break;

            case R.id.main_start_meeting:
                Intent intent3 = new Intent(MainActivity.this,Conference_pre.class);
                startActivity(intent3);
                break;
        }
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
                                ArrayList<String> mid = new ArrayList<String>();

                                for(int i =0; i<jsonArray.length();i++){
                                    mid.add(jsonArray.getJSONObject(i).getString("_id"));
                                    Log.d("mid",mid.get(i));
                                }
                                num_of_conference.setText(String.valueOf(mid.size()));
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


}
