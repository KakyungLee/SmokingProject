package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kakyunglee.smokingproject.R;

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

        // 리스트 내용은 listNeun에 담기

        //end 서버에서 공지사항 리스트 가져오기

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listMenu);
        ListView listView = (ListView)findViewById(R.id.list_notice);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent  intent = new Intent(NoticeListActivity.this, NoticeDetailActivity.class);
                intent.putExtra("title",listMenu[position]);
                startActivity(intent);
            }

        });
    }
}
