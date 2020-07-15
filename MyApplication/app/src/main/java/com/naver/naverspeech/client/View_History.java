package com.naver.naverspeech.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class View_History extends Fragment {

//    private TextView subject;
//    private TextView summary;

    private View recyleview;
    private Activity root;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;
    private ArrayList<String> mid = new ArrayList<>();
    private ArrayList<String> con_date = new ArrayList<>();
    private ArrayList<String> con_summarize = new ArrayList<>();
    private ArrayList<String> con_made = new ArrayList<>();
    private ArrayList<String> con_mname = new ArrayList<>();
    private ArrayList<String> con_where = new ArrayList<>();
    private ArrayList<ArrayList<String>> focus_summarize = new ArrayList<>();
    private String madename=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyleview = inflater.inflate(R.layout.recycle,container,false);
        mRecyclerView = (RecyclerView) recyleview.findViewById(R.id.myrecycler_view);
        root =getActivity();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(recyleview.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        myDataset = new ArrayList<>();
        mAdapter = new History_Feed(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        getMid();
        if(madename!=null&&con_date.size()!=0&&con_summarize.size()!=0) {
            Log.d("madename1", madename + " " + con_date.get(0) + " " + con_summarize.get(0));
        }
Cookie cookie = (Cookie) root.getApplication();
        // 회의 만든사람, 회의 대표이미지

//        myDataset.add(new MyData("ㄹ","ㄹ","주최: "+"팀장쓰",R.drawable.history_image2));
//        myDataset.add(new MyData("ㄹ","ㄹ","주최: "+"송규빈",R.drawable.history_image3));
        Log.d("뷰히스토리 순서","으악!!!!");
        return recyleview;
    }
    private void getMid(){

        Cookie cookie = (Cookie) root.getApplication();
        final RequestQueue queue = Volley.newRequestQueue(root.getApplicationContext());
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/info?unum="+cookie.getCookie()+"&done="+true;
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
                                Log.e("예약된 회의 멤버 불러오기 실패", response.getString("message"));
//                                //Toast.makeText(root.getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("예약된 회의 멤버 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                Log.d("데이터"," " +jsonArray.toString());
                                Log.d("개수"," "+jsonArray.length());
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject data_Object = new JSONObject(jsonArray.getJSONObject(i).toString());
                                    Log.d("data_array"," " +data_Object);
                                     mid.add(data_Object.getString("_id"));
                                    Log.d("mid",mid.get(i));

                                }
//                                //Toast.makeText(root.getApplicationContext(), " 예약된 회의 멤버 불러오기 성공", //Toast.LENGTH_LONG).show();
                                for(int i=0;i<mid.size();i++) {
                                    Log.d("프린트미드뷰",mid.get(i));
                                    printHistory(mid.get(i));
                                }
                            }
                        } catch (JSONException e) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
//                            //Toast.makeText(root.getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("예약된 회의 멤버 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("예약된 회의 멤버 불러오기 실패", error.toString());
//                        //Toast.makeText(root.getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);


    }
    private void printHistory(String mid){

        final Cookie cookie = (Cookie) root.getApplication();
        final RequestQueue queue = Volley.newRequestQueue(root.getApplicationContext());
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/result?mid="+mid;
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
                                Log.e("뷰히스토리정보 불러오기 실패", response.getString("message"));
//                                //Toast.makeText(root.getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("뷰히스토리정보 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                JSONArray jsonArray = new JSONArray(response.getJSONArray("summarize").toString());
                                Log.d("뷰히스토리 서머리"," " +jsonArray);
                                for(int i=0;i<jsonArray.length();i++){
                                    for(int j=0;j<jsonArray.getJSONArray(i).length();j++){
                                        con_summarize.add(jsonArray.getJSONArray(i).getString(j));
                                       Log.d("뷰히스토리정보 콘 서머리", con_summarize.get(j));
                                    }
                                    focus_summarize.add(con_summarize);
                                   for(int k =0;k<focus_summarize.get(0).size();k++){
                                        Log.d("뷰히스토리 포커스 서머리",focus_summarize.get(0).get(k));
                                   }
                                   con_mname.add(response.getString("mname"));
                                   Log.d("뷰히스토리 메이드",con_mname.get(i));
                                   con_where.add(response.getString("where"));
                                    Log.d("뷰히스토리 웨얼",con_where.get(i));
                                   con_date.add(response.getString("date").substring(0,10));
                                    Log.d("뷰히스토리 데이트",con_date.get(i));
                                    con_made.add(response.getJSONObject("made").getString("uname"));
                                    Log.d("뷰히스토리 메이드",response.getJSONObject("made").getString("uname"));
                                }
                                for(int h=0;h<con_made.size();h++) {
                                    myDataset.add(new MyData(con_date.get(h), con_mname.get(h), con_made.get(h), R.drawable.history_image1,focus_summarize.get(0)));
                                    mAdapter.notifyDataSetChanged();

                                }
//                                myDataset.add(new MyData(con_date.get(0),con_made.get(0),"주최: "+"팀장쓰",R.drawable.history_image2));
//                                mAdapter.notifyDataSetChanged();
//                                Log.d("핵심주제",focus_summarize.get(0).get(0));
//                                Log.d("핵심주제",focus_summarize.get(0).get(1));
//                                Log.d("주제1",jsonArray.getJSONArray(0).toString());
//                                Log.d("주제2",jsonArray.getJSONArray(1).toString());
//                                Log.d("주제1-1",jsonArray.getJSONArray(1).getString(0));
//                                Log.d("주제1-2",jsonArray.getJSONArray(1).getString(1));
//                                Log.d("made",""+response.getJSONObject("made"));
//                                Log.d("made1",""+response.getJSONObject("made").getString("uname"));
//                                con_made.add(response.getJSONObject("made").getString("uname"));
//                                Log.d("date",response.getString("date").substring(0,10));
//                                con_date.add(response.getString("date").substring(0,10));
//                                Log.d("라라",con_made.get(0)+" "+con_date.get(0));
//                                cookie.setMeetingTime(con_date.get(0),con_made.get(0));
//                                myDataset.add(new MyData(con_date.get(0),con_made.get(0),"주최: "+"팀장쓰",R.drawable.history_image2));
//                                mAdapter.notifyDataSetChanged();
//
//
//
// con_made.add(getUser(f));
//                                Log.d("con_made",con_made.get(0));
//                                //Toast.makeText(root.getApplicationContext(), " 예약된 회의 멤버 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
//                            //Toast.makeText(root.getApplicationContext(), "알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("예약된 회의 멤버 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("예약된 회의 멤버 불러오기 실패", error.toString());
//                        //Toast.makeText(root.getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);



    }



//    private String getUser(int u_made){
//
//        HashMap<String, String> params = new HashMap<String,String>();
//        params.put("unum",String.valueOf(u_made));
//
//        Cookie cookie = (Cookie) root.getApplication();
//        final RequestQueue queue = Volley.newRequestQueue(root.getApplicationContext());
//        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/valid/user";
//        Log.i("url", url);
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
//                                Log.e("주최자 정보 불러오기 실패", response.getString("message"));
//                                //Toast.makeText(root.getApplicationContext(), response.getString("message"), //Toast.LENGTH_LONG).show();
//                            } else { // 메세지 없으면 성공한거
//                                Log.v("주최자 정보 불러오기 성공", "success");
//                                Log.i("response", response.toString());
//
//                                madename = response.getString("uname");
//                                Log.d("madename"," "+madename);
//
//                                //Toast.makeText(root.getApplicationContext(), " 주최자 정보 불러오기 성공", //Toast.LENGTH_LONG).show();
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
//                        Log.e("주최자 정보 불러오기 실패", "인터넷 연결을 확인해주세요.");
//                        Log.e("주최자 정보 불러오기 실패", error.toString());
//                        //Toast.makeText(root.getApplicationContext(), "인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
//                    }
//                });
//        // 요청 큐에 추가하기
//        queue.add(jsonObjectRequest);
//
//        return madename;
//    }

}
