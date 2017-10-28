package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.dto.NoticeDTO;

/**
 * Created by KakyungLee on 2017-10-23.
 */

public class NoticeDetailActivity extends AppInfoActivity{


    NoticeDTO notice;
    TextView title;
    TextView created_at;
    TextView contnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail);
        Intent intent = getIntent();
        notice = (NoticeDTO) intent.getExtras().getSerializable("notice_detail");

        setTitle("공지사항");

        title = (TextView)findViewById(R.id.notice_title);
        created_at = (TextView)findViewById(R.id.notice_created_at);
        contnet = (TextView)findViewById(R.id.notice_content);

        title.setText(notice.getTitle());
        created_at.setText(notice.getCreated_at());
        contnet.setText(notice.getContents());

    }

}
