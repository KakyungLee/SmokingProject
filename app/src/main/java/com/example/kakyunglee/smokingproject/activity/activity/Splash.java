package com.example.kakyunglee.smokingproject.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.kakyunglee.smokingproject.R;

/**
 * Created by ckj on 2017-10-22.
 */

public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //추후 작업 방향에 따라 async task 추가 될 수 있음
        /*
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //intent.putExtra("userLoc",userLoc);
        */

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* 메뉴액티비티를 실행하고 로딩화면을 죽인다.*/
                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}

