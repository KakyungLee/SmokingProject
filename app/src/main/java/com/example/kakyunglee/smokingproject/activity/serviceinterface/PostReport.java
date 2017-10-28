package com.example.kakyunglee.smokingproject.activity.serviceinterface;

import com.example.kakyunglee.smokingproject.activity.dto.response.ReportDetailResultDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.ReportResultDTO;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Jeongyeji on 2017-10-23.
 */

public interface PostReport {
    //Post 요청을 보내는 예제 기본 골조는 GET메세지랑 비슷하나 Body 데이터를 어떻게 넘길 것인지 urlencoded는 값을 어떻게 주는지에 대한 방법을 알아본다
    @FormUrlEncoded
    @POST("SmokingServer/service/report")
    Call<ReportResultDTO> postSimpleReport(
            @FieldMap Map<String,String> params
            /*@Field("latitude") String latitude,
            @Field("longitude") String longitude*/
    );

    @Multipart
    @POST("SmokingServer/service/report/detail")
    Call<ReportDetailResultDTO> postDetailReport(
            @Part("report_category_id") int report_category_id,
            @Part("email") String email,
            @Part("contents") String contents,
            @Part MultipartBody.Part image,
            @Part("report_detail_id") int report_detail_id
    );
}
