package com.naver.naverspeech.client;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class History_Feed extends RecyclerView.Adapter<History_Feed.ViewHolder> {
    private ArrayList<MyData> mDataset;
    private Context context;
    private History_Adapter adapter;
    private ArrayList<List_m_Item> data = new ArrayList<>();
    private List_m_Item list_m_item;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView view_image;
        private TextView view_made;
        private TextView view_date;
        private TextView view_mname;
        private ListView focus_subject_list;
        private TextView addsee;
        private LinearLayout view_history_layout;
        public ViewHolder(View view) {
            super(view);
            view_image = (ImageView)view.findViewById(R.id.view_Image);
            view_made = (TextView)view.findViewById(R.id.view_Made);
            view_date = (TextView)view.findViewById(R.id.view_Date);
            view_mname = (TextView)view.findViewById(R.id.view_Mname);
            focus_subject_list = (ListView)view.findViewById(R.id.focus_subject);
            addsee = (TextView) view.findViewById(R.id.addsee);
            view_history_layout = (LinearLayout) view.findViewById(R.id.view_history_layout);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public History_Feed(ArrayList<MyData> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public History_Feed.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_history, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.view_date.setText(mDataset.get(position).date);
        holder.view_mname.setText(mDataset.get(position).mname);
        holder.view_made.setText(mDataset.get(position).made);
        holder.view_image.setImageResource(mDataset.get(position).img);

        context = holder.view_date.getContext();
        adapter = new History_Adapter(context,R.layout.list_history_feed,data);
        holder.focus_subject_list.setAdapter(adapter);
        for(int i=0;i<mDataset.get(position).focus_summarize.size();i++) {
            list_m_item = new List_m_Item(mDataset.get(position).focus_summarize.get(i));
            Log.d("히스토리 피드"," "+mDataset.get(position).focus_summarize.size());
            data.add(list_m_item);
            adapter.notifyDataSetChanged();
        }

        if(data.size()>2){
            holder.addsee.setVisibility(View.VISIBLE);
            holder.addsee.setEnabled(true);
            holder.addsee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context,History_Add_See.class);
                    intent.putExtra("view_date",holder.view_date.getText().toString());
                    intent.putExtra("view_mname",holder.view_mname.getText().toString());
                    intent.putExtra("view_made",holder.view_made.getText().toString());
                    intent.putExtra("view_image",holder.view_image.getDrawable().toString());
                    intent.putExtra("view_position",position);
                    intent.putExtra("view_focus_summarize",mDataset.get(position).focus_summarize);
                    context.startActivity(intent);
                }
            });


        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
class MyData{
    public String date;
    public String mname;
    public String made;
    public int img;
    public ArrayList<String> focus_summarize = new ArrayList<>();

    public MyData(String date, String mname, String made, int img,ArrayList<String> focus_summarize){
        this.date = date;
        this.mname = mname;
        this.made = made;
        this.img =img;
        this.focus_summarize = focus_summarize;
    }
    public MyData(String date,String subject,String mname){
        this.date = date;
        this.mname = mname;
    }
    public MyData(String date,String mname, int img){
        this.date = date;
        this.mname = mname;
        this.img = img;
    }
    public MyData(String made, int img){
        this.made = made;
        this.img = img;
    }
    public MyData(String date,String mname){
        this.date = date;
        this.mname = mname;
    }

    public String getMname() {
        return mname;
    }
}
