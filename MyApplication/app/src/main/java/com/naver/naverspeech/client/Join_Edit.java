package com.naver.naverspeech.client;

import android.content.Context;
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

public class Join_Edit extends AppCompatActivity {

    private SparseBooleanArray checkedlist_num;
    private int count;
    private int del_fnum;
    private String del_fname;
    private String del_fcompany;
    private String del_fdepartment;
    private String del_fposition;
    private String del_department;
    private Drawable del_f_drawble;
    private Context context;

    private ArrayList<List_Item> data = new ArrayList<>();
    private Drawable friend_image;
    private List_Item list_item;
    private List_MakeGroupAdapter adapter;
    private ListView join_edit_list;
    private Button delete_friend;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_edit);


        context = this;
        join_edit_list = (ListView) findViewById(R.id.join_edit_list);
        friend_image  = this.getResources().getDrawable(R.drawable.account1);
        adapter = new List_MakeGroupAdapter(this, R.layout.list_make_group_friends, data);
        delete_friend = (Button) findViewById(R.id.delete_freind);

        printFavorite();
        join_edit_list.setAdapter(adapter);

        delete_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedlist_num = join_edit_list.getCheckedItemPositions();
                count = adapter.getCount();
                if(checkedlist_num ==null){
//                    //Toast.makeText(getApplicationContext(),"삭제할 친구를 선택해주세요",//Toast.LENGTH_LONG).show();
                }
                for (int i = count-1;i>=0;i--){
                    if(checkedlist_num.get(i)){
                        Log.d("삭제될 아이템"," "+data.get(i).getFnum());
                        deleteFavorite(data.get(i).getFnum());
                        data.remove(i);
                    }
                }
                join_edit_list.clearChoices();
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void printFavorite() {
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
                                Log.e("삭제 계정 불러오기 실패", response.getString("message"));
//                                //Toast.makeText(getApplicationContext(),response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("삭제 계정 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                System.out.println(response.get("data"));
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                for(int i =0; i<jsonArray.length();i++) {

                                    JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                                    del_fname = dataJsonObj.getString("uname");
                                    del_fnum = dataJsonObj.getInt("unum");
                                    del_fcompany = dataJsonObj.getString("company");
                                    del_department = dataJsonObj.getString("department");

                                    del_f_drawble = context.getResources().getDrawable(R.drawable.account1);
                                    list_item = new List_Item(del_fname,del_fcompany,del_department,del_fnum,del_f_drawble);
                                    data.add(list_item);
                                    adapter.notifyDataSetChanged();
                                }

//                                //Toast.makeText(getApplicationContext(),"삭제 계정 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch(JSONException e){
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
//                            //Toast.makeText(getApplicationContext(),"알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("삭제 계정 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("삭제 계정 불러오기 실패", error.toString());
//                        //Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }
    private void deleteFavorite(int fnum){

        Cookie cookie = (Cookie) getApplication();

        HashMap<String, String> params = new HashMap<String,String>();
        params.put("unum", String.valueOf(cookie.getCookie()));
        params.put("fnum", String.valueOf(fnum));
        Log.d("deleteFavorite Cookie:"," "+cookie.getCookie());

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/add/favorite";
        Log.i("url", url);

        // 일단 시작
        queue.start();

        // 요청 만들기
        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    // 제대로 된 응답이 들어왔을 시
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.has("message")) { // 메세지 가지고 있음 에러난거
                                // 오류를 사용자에게 알리도록 고칠 것.
                                // message에 있는 내용을 알릴 것.
                                Log.e("즐겨찾는 계정 삭제 실패", response.getString("message"));
//                                //Toast.makeText(getApplicationContext(),response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("즐겨찾는 계정 삭제 성공", "success");
                                Log.i("response", response.toString());
                                System.out.println(response.get("data"));

//                                //Toast.makeText(getApplicationContext(),"즐겨찾는 계정 삭제 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch(JSONException e){
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
//                            //Toast.makeText(getApplicationContext(),"알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
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
//                        //Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);

    }


}
