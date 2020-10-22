package com.example.rxjavalog.model

import com.example.rxjavalog.adapter.EmptyStringToNumberTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val BASE_URL_API = "https://openapi.naver.com/"

class NaverMovieClient {
    fun getGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Int::class.java, EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Integer::class.java, EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Double::class.java, EmptyStringToNumberTypeAdapter())

        val gson: Gson = gsonBuilder.create()

        return gson
    }

    val retrofit: NaverMovieApi = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .build().create(NaverMovieApi::class.java)
}