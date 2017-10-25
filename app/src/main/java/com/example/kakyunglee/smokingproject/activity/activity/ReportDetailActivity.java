package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kakyunglee.smokingproject.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class ReportDetailActivity extends AppCompatActivity{

    private final int PICK_FROM_CAMERA =0;
    private final int PICK_FROM_ALBUM =1;
    private final int CROP_FROM_IMAGE= 2;

    private Uri mImageCaptureUir;
    private ByteArrayOutputStream byteBuff;
    private byte[] byteArray;
    InputStream is = null;

    /////////////////////////////////////////
    private Spinner spinner;
    private ImageView loadImage;
    private ImageButton btnGallery;
    private ImageButton btnCamera;
    private EditText editText;
    private Button button;
    /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail);

        //엑션바 사용자 커스텀 타이틀 설정
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        TextView title = (TextView)findViewById(R.id.title_bar);
        title.setText("상세 신고");

        //xml 객체 연결
        spinner = (Spinner)findViewById(R.id.report_spinner);
        editText = (EditText) findViewById(R.id.report_detail_content);
        loadImage = (ImageView)findViewById(R.id.load_image) ;
        btnGallery = (ImageButton)findViewById(R.id.open_gallery);
        btnCamera = (ImageButton)findViewById(R.id.open_camera);
        button = (Button)findViewById(R.id.submit_report);

        // 스피너 어댑터
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.report_detail,android.R.layout.simple_spinner_dropdown_item);
        spinner.setDropDownVerticalOffset(120);
        spinner.setAdapter(adapter);

        // 갤러리에서 이미지 불러오기 버튼
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                Toast.makeText(getApplicationContext(), "get image from gallery", Toast.LENGTH_LONG).show();
            }
        });

        // 카메라에서 이미지 불러오기 버튼
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                Toast.makeText(getApplicationContext(), "get image from camera", Toast.LENGTH_LONG).show();
            }
        });

        // 입력한 정보를 서버로 전송하는 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),spinner.getSelectedItemPosition()+"",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url =  MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,url);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    public  void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode){
            case PICK_FROM_CAMERA :
               Bundle extras = data.getExtras();
                if(extras != null){
                    Bitmap bitmap = extras.getParcelable("data");
                    loadImage.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byteArray = stream.toByteArray();
                }

                break;
            case PICK_FROM_ALBUM :
                mImageCaptureUir = data.getData();
                loadImage.setImageURI(mImageCaptureUir);
                try {
                    is = getContentResolver().openInputStream(data.getData());
                    getBytes(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }


    }

    public byte[] getBytes(InputStream is)  {
        byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        try {
            while ((len = is.read(buff)) != -1) {
                byteBuff.write(buff, 0, len);
            }
        }catch(IOException e){}
        return byteBuff.toByteArray();
    }

}
