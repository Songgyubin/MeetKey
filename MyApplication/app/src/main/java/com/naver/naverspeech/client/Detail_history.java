package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Detail_history extends Activity {

    TextView subject;
    TextView date;
    TextView people;
    TextView room;

    Button summary;
    Button play;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_history);

        subject = (TextView) findViewById(R.id.history_subject);
        date = (TextView) findViewById(R.id.history_date);
        people = (TextView) findViewById(R.id.history_people);
        room = (TextView) findViewById(R.id.conference_room);

        summary = (Button) findViewById(R.id.view_summary);
        play = (Button) findViewById(R.id.play_voice);
        delete = (Button) findViewById(R.id.delete);

        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_history.this, View_History.class);
                startActivity(intent);
            }
        });

    }
}
