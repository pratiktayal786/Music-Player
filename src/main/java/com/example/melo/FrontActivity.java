package com.example.melo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FrontActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FrontActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);


    }
}