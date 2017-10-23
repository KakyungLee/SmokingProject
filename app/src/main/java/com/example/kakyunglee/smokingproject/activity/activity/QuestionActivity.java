package com.example.kakyunglee.smokingproject.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.kakyunglee.smokingproject.R;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);

        Spinner spinner = (Spinner)findViewById(R.id.question_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.question_detail,android.R.layout.simple_spinner_dropdown_item);
        spinner.setDropDownVerticalOffset(150);
        spinner.setAdapter(adapter);
    }
}

