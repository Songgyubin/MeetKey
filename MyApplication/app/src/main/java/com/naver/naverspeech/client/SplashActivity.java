package com.naver.naverspeech.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(3000);
            Intent mainIntent = new Intent(this,Login.class);
            startActivity(mainIntent);
            finish();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
