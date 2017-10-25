package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.dto.QuestionDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.QuestionResponseDTO;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.PostQuestion;
import com.example.kakyunglee.smokingproject.activity.util.ServiceRetrofit;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.kakyunglee.smokingproject.R.layout.question;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class QuestionActivity extends AppCompatActivity {

    private final int PICK_FROM_CAMERA = 0;
    private final int PICK_FROM_ALBUM = 1;
    private final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUir;
    private ImageView loadImage;
    private ByteArrayOutputStream byteBuff;

    //////////////////////////////////////////////
    private EditText questionTitle;
    private EditText questionContent;
    private EditText questionEmail;
    InputStream is = null;
    private Button postImage;
    //////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(question);

        setTitle("정부 문의");
        ///////////////////////////////////////////////////////////////////
        questionTitle = (EditText) findViewById(R.id.question_title_edit);
        questionContent = (EditText) findViewById(R.id.question_content_edit);
        questionEmail = (EditText) findViewById(R.id.question_email_edit);
        postImage = (Button) findViewById(R.id.postquest_btn);
        /////////////////////////////////////////////////////////////////////

        Spinner spinner = (Spinner) findViewById(R.id.question_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.question_detail, android.R.layout.simple_spinner_dropdown_item);
        spinner.setDropDownVerticalOffset(120);
        spinner.setAdapter(adapter);

        // 스피너 클릭 리스너 만들기

        // question_detail_id 얻을 수 있게 해주세요

        loadImage = (ImageView) findViewById(R.id.load_image_question);

        ImageButton btnGallery = (ImageButton) findViewById(R.id.open_gallery_question);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                Toast.makeText(getApplicationContext(), "get image from gallery", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton btnCamera = (ImageButton) findViewById(R.id.open_camera_question);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                Toast.makeText(getApplicationContext(), "get image from camera", Toast.LENGTH_LONG).show();
            }
        });


        //////////////////////////////////////////
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionDTO questionDto=new QuestionDTO();
                questionDto.setTitle(questionTitle.getText().toString());
                questionDto.setReport_category_id(1);
                questionDto.setContents(questionContent.getText().toString());
                questionDto.setEmail(questionEmail.getText().toString());
                if(is!=null) postTotalData(getBytes(is),questionDto);
                else postTotalData(questionDto);

            }
        });
        //////////////////////////////////////////
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, url);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap = extras.getParcelable("data");
                    loadImage.setImageBitmap(bitmap);
                }
                break;
            case PICK_FROM_ALBUM:
                mImageCaptureUir = data.getData();
                loadImage.setImageURI(mImageCaptureUir);
               // InputStream is = null;
                try {
                    is = getContentResolver().openInputStream(mImageCaptureUir);
                    //getBytes(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
///////////////////////// 다소 수정 ///////////////////////////
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


    //////////////////////////////////////////////////////////////////////////
    private void postTotalData(byte[] imageBytes, QuestionDTO inputData) {
        PostQuestion postReportService = ServiceRetrofit.getInstance().getRetrofit().create(PostQuestion.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        String mimeType = requestFile.contentType().toString();
        String newImage = new Date().toString().replaceAll(" ","")+"image." + mimeType.substring(mimeType.indexOf("/") + 1, mimeType.length());
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", newImage, requestFile);
        final Call<QuestionResponseDTO> call = postReportService.postQuestion(inputData.getTitle(),inputData.getReport_category_id(),inputData.getContents(),inputData.getEmail(),body);
        new postQuestionCall().execute(call);

    }

    private void postTotalData(QuestionDTO inputData){

        PostQuestion postReportService = ServiceRetrofit.getInstance().getRetrofit().create(PostQuestion.class);
        final Call<QuestionResponseDTO> call = postReportService.postQuestion(inputData.getTitle(),inputData.getReport_category_id(),inputData.getContents(),inputData.getEmail(),null);
        new postQuestionCall().execute(call);

    }
    ////////////////////////////////////////////////////////////////////////////////
    private class postQuestionCall extends AsyncTask<Call,Void, QuestionResponseDTO> {
        @Override
        protected QuestionResponseDTO doInBackground(Call ... params){
            try{
                Call<QuestionResponseDTO> call = params[0];
                Response response = call.execute();
                return (QuestionResponseDTO) response.body();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(QuestionResponseDTO result) {
            Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
        }

    }
}


