package com.example.rxjavalog.model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// 주의! : CLIENT_ID, CLIENT_SECRET api key 는 Hidden 처리 해야함
private const val BASE_URL_API = "https://openapi.naver.com/"
private const val CLIENT_ID = "API_KEY_Hidden"
private const val CLIENT_SECRET = "API_KEY_Hidden"

class NaverMovieClient {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient())
        .build().create(NaverMovieApi::class.java)

    val callGetSearchMovie =
        retrofit.getSearchMovie(CLIENT_ID, CLIENT_SECRET, "고양이", 50, 1)

}