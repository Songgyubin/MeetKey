package com.naver.naverspeech.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class History_Add_See extends AppCompatActivity {
    private ImageView view_add_image;
    private TextView view_add_made ;
    private TextView view_add_date;
    private TextView view_add_mname;
    private ListView view_add_focus_subject_list;

    private History_Adapter adapter;
    private ArrayList<List_m_Item> data = new ArrayList<>();
    private List_m_Item list_m_item;
    private ArrayList<MyData> mDataset;
    private ArrayList<String> focus_summarize = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_add_see);

        view_add_image = (ImageView)findViewById(R.id.view_add_Image);
        view_add_made = (TextView)findViewById(R.id.view_add_Made);
        view_add_date = (TextView)findViewById(R.id.view_add_Date);
        view_add_mname = (TextView)findViewById(R.id.view_add_Mname);
        view_add_focus_subject_list = (ListView)findViewById(R.id.view_add_focus_subject);

        Intent intent = getIntent();
        view_add_made.setText(intent.getStringExtra("view_made"));
        view_add_date.setText(intent.getStringExtra("view_date"));
        view_add_mname.setText(intent.getStringExtra("view_mname"));
        focus_summarize = intent.getStringArrayListExtra("view_focus_summarize");
        adapter = new History_Adapter(this,R.layout.list_history_feed,data);
        view_add_focus_subject_list.setAdapter(adapter);
        for(int i=0;i<focus_summarize.size();i++) {
            list_m_item = new List_m_Item(focus_summarize.get(i));
            Log.d("히스토리 피드"," "+focus_summarize.size());
            data.add(list_m_item);
            adapter.notifyDataSetChanged();
        }


    }
}
