package com.example.kakyunglee.smokingproject.activity.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chakh on 2017-10-12.
 */
public class HungryDuckRetrofit {
    private static HungryDuckRetrofit instance = new HungryDuckRetrofit();
    private Retrofit retrofit;
    final String serverURI = "http://172.30.1.20:8080/SmokingServer";
    private HungryDuckRetrofit(){
        buildRetrofit();
    }
    private void buildRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(serverURI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static HungryDuckRetrofit getInstance(){
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
