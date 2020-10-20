package com.example.rxjavalog.model

import com.example.rxjavalog.Adapter.EmptyStringToNumberTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// 주의! : CLIENT_ID, CLIENT_SECRET api key 는 Hidden 처리 해야함
private const val BASE_URL_API = "https://openapi.naver.com/"


class NaverMovieClient {
    fun getGson() : Gson{
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Int::class.java, EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Integer::class.java, EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Double::class.java, EmptyStringToNumberTypeAdapter())

        val gson : Gson = gsonBuilder.create()

        return gson
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient())
        .build().create(NaverMovieApi::class.java)
}