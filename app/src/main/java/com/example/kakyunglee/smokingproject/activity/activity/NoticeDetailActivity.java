package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.dto.NoticeDTO;

/**
 * Created by KakyungLee on 2017-10-23.
 */

public class NoticeDetailActivity extends AppInfoActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail);
        Intent intent = getIntent();

      //  setTitle(intent.getExtras().getString("title"));


        NoticeDTO noticeDto=(NoticeDTO)intent.getSerializableExtra("notice_detail");
        setTitle(noticeDto.getTitle());
        // 상세 공지사항 내용 서버에서 가져 오기



    }

}
