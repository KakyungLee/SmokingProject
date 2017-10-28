package com.example.kakyunglee.smokingproject.activity.dto.response;

import java.io.Serializable;

/**
 * Created by Jeongyeji on 2017-10-24.
 */

public class ReportResultDTO implements Serializable{
    private int id;
    private int count;
    public int getId(){
        return id;
    }
    @Override
    public String toString() {
        return "ReportResultDTO{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}
