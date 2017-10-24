package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.dto.NoticeDTO;
import com.example.kakyunglee.smokingproject.activity.dto.NoticeListDTO;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.GetNoticeInfo;
import com.example.kakyunglee.smokingproject.activity.util.ServiceRetrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class NoticeListActivity extends AppCompatActivity{

    private String[] listMenu={"test1","test2","test3"};
    private int detail_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

        setTitle("공지사항");

        // 서버에서 공지사항 리스트 가져오기
        NoticeListDTO noticeListDto = (NoticeListDTO) this.getIntent().getExtras().get("notice_list");
        for (NoticeDTO notice : noticeListDto.noticeLists) {
            System.out.println(notice.getTitle());
        }
        // 리스트 내용은 listNeun에 담기

        //end 서버에서 공지사항 리스트 가져오기

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listMenu);
        ListView listView = (ListView) findViewById(R.id.list_notice);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                detail_id = position;
                GetNoticeInfo designerInfoService = ServiceRetrofit.getInstance().getRetrofit().create(GetNoticeInfo.class);
                final Call<NoticeDTO> call = designerInfoService.noticeDetailInfo(position);
                // 이 번호를 입력한다.


                new getNoticeDetail().execute(call);
            }

        });
    }
        private class getNoticeDetail extends AsyncTask<Call,Void,NoticeDTO> {
            @Override
            protected NoticeDTO doInBackground(Call ... params){
                try{
                    Call<NoticeDTO> call = params[0];
                    Response<NoticeDTO> response = call.execute();
                    return response.body();
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(NoticeDTO result) {
                Intent intent = new Intent(NoticeListActivity.this, NoticeDetailActivity.class);
                intent.putExtra("notice_detail",result);
                startActivity(intent);


        }
    }
}
