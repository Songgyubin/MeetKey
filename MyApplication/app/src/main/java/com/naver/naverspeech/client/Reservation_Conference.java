package com.naver.naverspeech.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class Reservation_Conference extends Fragment {

    private View view;
    private Activity root;

    private LinearLayout setDate;
    private LinearLayout setTime;
    private TextView reservation_hour;
    private TextView reservation_minute;
    private TextView reservation_year;
    private TextView reservation_month;
    private TextView reservation_day;
    private TextInputEditText reservation_mname;
    private TextInputEditText reservation_where;
    private TextInputEditText reservation_memo;
    private ListView reservation_mem_list;
    private Button reservation;
    private ImageButton reservation_con_gohome;


    private ArrayList<List_Item> data = new ArrayList<>();
    private List_MakeGroupAdapter adapter;
    private List_Item list_item;
    private String reservation_fname;
    private int reservation_fnum;
    private String reservation_fcompany;
    private String reservation_department;
    private Drawable reservation_f_drawble;
    private Context context;
    private SparseBooleanArray checkedlist_num;
    private int count;
    private ArrayList<Integer> reservation_fnums = new ArrayList<>();
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;
    private String mid;
    private Cookie cookie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reservation_conference,container,false);
        root = getActivity();

        cookie = (Cookie) root.getApplication();

        setDate = (LinearLayout) view.findViewById(R.id.reservation_Date);
        setTime = (LinearLayout) view.findViewById(R.id.reservation_Time);
        reservation_mname = (TextInputEditText) view.findViewById(R.id.reservation_mname);
        reservation_where = (TextInputEditText) view.findViewById(R.id.reservation_where);
        reservation_memo = (TextInputEditText) view.findViewById(R.id.reservation_memo);
        reservation_mem_list = (ListView) view.findViewById(R.id.reservation_mem_list);
        reservation = (Button) view.findViewById(R.id.reservation);
        reservation_hour = (TextView) view.findViewById(R.id.reservation_hour);
        reservation_minute = (TextView) view.findViewById(R.id.reservation_minute);
        reservation_year = (TextView) view.findViewById(R.id.reservation_year);
        reservation_month = (TextView) view.findViewById(R.id.reservation_month);
        reservation_day = (TextView) view.findViewById(R.id.reservation_day);
        reservation_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment1 = new Reservation_Conference.DatePickerFragment();
                newFragment1.show(getFragmentManager(), "datePicker");
            }
        });
        reservation_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment1 = new Reservation_Conference.DatePickerFragment();
                newFragment1.show(getFragmentManager(), "datePicker");
            }
        });
        reservation_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment1 = new Reservation_Conference.DatePickerFragment();
                newFragment1.show(getFragmentManager(), "datePicker");
            }
        });
        reservation_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment4 = new Reservation_Conference.TimePickerFragment();
                newFragment4.show(getFragmentManager(), "timePicker");
            }
        });
        reservation_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment4 = new Reservation_Conference.TimePickerFragment();
                newFragment4.show(getFragmentManager(), "timePicker");
            }
        });

        SimpleDateFormat time = new SimpleDateFormat("hhmmss");
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");

        reservation_hour.setText(time.format(new Date()).substring(0,2));
        reservation_minute.setText(time.format(new Date()).substring(2,4));
        reservation_year.setText(date.format(new Date()).substring(0,4));
        reservation_month.setText(date.format(new Date()).substring(4,6));
        reservation_day.setText(date.format(new Date()).substring(6));

//        year = reservation_year.getText().toString();
//        month = reservation_month.getText().toString();
//        day = intent.getStringExtra("day");
//        hour = intent.getStringExtra("hour");
//        minute = intent.getStringExtra("minute");
//        second = intent.getStringExtra("second");

        printReservationFriends();
        adapter = new List_MakeGroupAdapter(view.getContext(), R.layout.list_make_group_friends, data);
        reservation_mem_list.setAdapter(adapter);

        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkedlist_num = reservation_mem_list.getCheckedItemPositions();
                count = adapter.getCount();
                if(checkedlist_num ==null){
                    Toast.makeText(view.getContext(),"추가할 친구를 선택해주세요",Toast.LENGTH_LONG).show();
                }
                for (int i = count-1;i>=0;i--){
                    if(checkedlist_num.get(i)){
                        Log.d("추가될 아이템"," "+data.get(i).getFnum());
                        Log.d("체크넘"," "+i);
                        reservation_fnums.add (Integer.valueOf(data.get(i).getFnum()+1));
                    }
                }
                reservation_fnums.add(cookie.getCookie());
                ReservationMeeting();

            }
        });
        return view;
    }
//    public void onClick(View v){
//        switch (v.getId()){
//
//            case R.id.reservation_Date:
//                DialogFragment newFragment = new Reservation_Conference.DatePickerFragment();
//                newFragment.show(getFragmentManager(), "datePicker");
//                Toast.makeText(root,"외않나와",Toast.LENGTH_LONG).show();
//                break;
//
//            case R.id.reservation_year:
//
//                Toast.makeText(root,"외않나와",Toast.LENGTH_LONG).show();
//                break;
//
//            case R.id.reservation_month:
//                DialogFragment newFragment2 = new Reservation_Conference.DatePickerFragment();
//                newFragment2.show(getFragmentManager(), "datePicker");
//                Toast.makeText(root,"외않나와",Toast.LENGTH_LONG).show();
//                break;
//
//            case R.id.reservation_day:
//                DialogFragment newFragment3 = new Reservation_Conference.DatePickerFragment();
//                newFragment3.show(getFragmentManager(), "datePicker");
//                Toast.makeText(root,"외않나와",Toast.LENGTH_LONG).show();
//                break;
//
//            case R.id.reservation_Time:
//                DialogFragment newFragment4 = new Reservation_Conference.TimePickerFragment();
//                newFragment4.show(getFragmentManager(), "timePicker");
//                Toast.makeText(root,"외않나와",Toast.LENGTH_LONG).show();
//                break;
//
//            case R.id.reservation_hour:
//                DialogFragment newFragment5 = new Reservation_Conference.TimePickerFragment();
//                newFragment5.show(getFragmentManager(), "timePicker");
//                Toast.makeText(root,"외않나와",Toast.LENGTH_LONG).show();
//                break;
//
//            case R.id.reservation_minute:
//                DialogFragment newFragment6 = new Reservation_Conference.TimePickerFragment();
//                newFragment6.show(getFragmentManager(), "timePicker");
//                Toast.makeText(root,"외않나와",Toast.LENGTH_LONG).show();
//                break;
//
//
//        }
//
//    }
    private void printReservationFriends(){

        Cookie cookie = (Cookie) root.getApplication();
        final RequestQueue queue = Volley.newRequestQueue(view.getContext());
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
                                Log.e("초대 계정 불러오기 실패", response.getString("message"));
                                //Toast.makeText(getApplicationContext(),response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("초대 계정 불러오기 성공", "success");
                                Log.i("response", response.toString());
                                System.out.println(response.get("data"));
                                JSONArray jsonArray = new JSONArray(response.get("data").toString());
                                for(int i =0; i<jsonArray.length();i++) {

                                    JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                                    reservation_fname = dataJsonObj.getString("uname");
                                    reservation_fnum = dataJsonObj.getInt("unum");
                                    reservation_fcompany = dataJsonObj.getString("company");
                                    reservation_department = dataJsonObj.getString("department");
                                    reservation_f_drawble = view.getContext().getDrawable(R.drawable.account1);
                                    list_item = new List_Item(reservation_fname,reservation_fcompany,reservation_department,reservation_fnum,reservation_f_drawble);
                                    data.add(list_item);

                                    adapter.notifyDataSetChanged();
                                }

                                //Toast.makeText(getApplicationContext(),"초대 계정 불러오기 성공", //Toast.LENGTH_LONG).show();
                            }
                        } catch(JSONException e){
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
                        Log.e("초대 계정 불러오기 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("초대 계정 불러오기 실패", error.toString());
                        //Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);
    }
    private void ReservationMeeting() {

        year = reservation_year.getText().toString();
        month = reservation_month.getText().toString();
        day = reservation_day.getText().toString();
        hour = reservation_hour.getText().toString();
        minute = reservation_minute.getText().toString();
        second = new SimpleDateFormat("ss").format(new Date());
        HashMap<String,Object> params = new HashMap<>();
        params.put("mname",reservation_mname.getText().toString());
        params.put("date",year+"-"+month+"-"+day);
        params.put("time",hour+":"+minute+":"+second);
        params.put("where",reservation_where.getText().toString());
        params.put("made",cookie.getCookie());
        params.put("memo",reservation_memo.getText().toString());
        params.put("member",reservation_fnums);

        final RequestQueue queue = Volley.newRequestQueue(view.getContext());
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
                                Log.e("예약회의생성 실패", response.getString("message"));
//                                //Toast.makeText(root,response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.v("예약회의생성 성공", "success");
                                Log.i("response", response.toString());
                                Log.d("예약성공했을때 mid",response.getString("_id"));
                                mid = response.getString("_id");
                                Toast.makeText(root,"회의 예약 성공", Toast.LENGTH_LONG).show();
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
                        Log.e("예약회의생성 실패", "인터넷 연결을 확인해주세요.");
                        Log.e("예약회의생성 실패", error.toString());
//                        //Toast.makeText(root,"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);

    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        private TextView reservation_hour;
        private TextView reservation_minute;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            reservation_hour = (TextView) getActivity().findViewById(R.id.reservation_hour);
            reservation_minute = (TextView) getActivity().findViewById(R.id.reservation_minute);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            reservation_hour.setText(String.valueOf(hourOfDay));
            reservation_minute.setText(String.valueOf(minute));
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private TextView reservation_year;
        private TextView reservation_month;
        private TextView reservation_day;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            reservation_year = (TextView) getActivity().findViewById(R.id.reservation_year);
            reservation_month = (TextView) getActivity().findViewById(R.id.reservation_month);
            reservation_day = (TextView) getActivity().findViewById(R.id.reservation_day);

            reservation_year.setText(String.valueOf(year));
            reservation_month.setText(String.valueOf(month+1));
            reservation_day.setText(String.valueOf(day));
        }
    }

}
