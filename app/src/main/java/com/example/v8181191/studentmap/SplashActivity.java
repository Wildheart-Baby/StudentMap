package com.example.v8181191.studentmap;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Wildheart on 16/03/2019.
 */

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() { }
        }.start();

        Intent intent = new Intent(this, CampusMap.class);
        startActivity(intent);
        finish();
    }
}
