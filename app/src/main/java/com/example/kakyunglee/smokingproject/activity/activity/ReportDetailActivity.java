package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kakyunglee.smokingproject.R;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class ReportDetailActivity extends AppCompatActivity{

    private final int PICK_FROM_CAMERA =0;
    private final int PICK_FROM_ALBUM =1;
    private final int CROP_FROM_IMAGE= 2;

    private Uri mImageCaptureUir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail);

        setTitle("상세 신고");

        Spinner spinner = (Spinner)findViewById(R.id.report_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.report_detail,android.R.layout.simple_spinner_dropdown_item);
        spinner.setDropDownVerticalOffset(120);
        spinner.setAdapter(adapter);

        ImageButton btnGallery = (ImageButton)findViewById(R.id.open_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                Toast.makeText(getApplicationContext(), "get image from gallery", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton btnCamera = (ImageButton)findViewById(R.id.open_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                Toast.makeText(getApplicationContext(), "get image from camera", Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void openCamera()
    {
        // 카메라 터짐..
        /*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "temp_"+ String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUir = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),url));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUir);
        startActivityForResult(intent,PICK_FROM_CAMERA);
        */
    }
    public  void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }



}
