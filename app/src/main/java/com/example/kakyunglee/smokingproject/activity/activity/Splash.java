package com.example.kakyunglee.smokingproject.activity.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.kakyunglee.smokingproject.R;

/**
 * Created by ckj on 2017-10-22.
 */

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //추후 작업 방향에 따라 async task 추가 될 수 있음

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //intent.putExtra("userLoc",userLoc);


    }
}
