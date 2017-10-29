package com.example.kakyunglee.smokingproject.activity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.kakyunglee.smokingproject.R;

/**
 * Created by ckj on 2017-10-22.
 */

public class Splash extends Activity {

    int check =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new MyAsyncTask().execute();

    }

        public void makePermissionDialog(final String permissionType){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Splash.this);
            dialog.setTitle("권한이 필요합니다.")
                    .setMessage("권한을 허용하지 않는다면 정상적인 앱 사용이 불가능 합니다. 권한을 허용하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{permissionType}, 1000); } } })
                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create()
                    .show();
        }

        public boolean getPermission(){
            // 퍼미션
            if(Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                };

                int[] permissionResult = {
                        checkSelfPermission(permissions[0]),
                        checkSelfPermission(permissions[1]),
                        checkSelfPermission(permissions[2])
                };

                if (permissionResult[0] == PackageManager.PERMISSION_DENIED) { // 위치 권한 설정

                    if(shouldShowRequestPermissionRationale(permissions[0])){ // 거부한적 있는 경우
                        makePermissionDialog(permissions[0]); // 권한 설정 재요청
                    }
                    else{
                        requestPermissions(new String[]{permissions[0]}, 1000); // 처음 권한 요청하는 경우
                    }

                }
                if (permissionResult[1] == PackageManager.PERMISSION_DENIED) { // 카메라 권한 설정
                    if(shouldShowRequestPermissionRationale(permissions[1])){ // 거부한적 있는 경우
                        makePermissionDialog(permissions[1]); // 권한 설정 재요청
                    }
                    else{
                        requestPermissions(new String[]{permissions[1]}, 1000); //처음 권한 요청하는 경우
                    }
                }
                if (permissionResult[2] == PackageManager.PERMISSION_DENIED) { // 갤러리 권한 설정정
                    if(shouldShowRequestPermissionRationale(permissions[2])){ //거부한적 있는 경우
                        makePermissionDialog(permissions[2]); // 권한 설정 재용청
                    }
                    else{
                        requestPermissions(new String[]{permissions[2]}, 1000); // 처음 권한 요청하는 경우
                    }
                }
            }

            return true;
        }

        class MyAsyncTask extends AsyncTask<Void, Void, Void>{

            boolean isTrue = false;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                //포그라운드에서 처리할때 사용
                isTrue = getPermission();


            }

            protected Void doInBackground(Void... params) {
               // 백그라운드에서 처리 할때 사용
                return null;

            }

            protected  void onPostExecute(Void result){
               super.onPostExecute(result);
                // 퍼미션 완료후 실행할 작업 선택
                    Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();

            }

        }

}

