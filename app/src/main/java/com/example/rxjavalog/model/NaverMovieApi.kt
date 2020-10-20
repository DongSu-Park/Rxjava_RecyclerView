package com.example.rxjavalog.model

import io.reactivex.Observable
import retrofit2.http.*

interface NaverMovieApi {
    @GET("v1/search/movie.json")
    fun getSearchMovie(
        @Header("X-Naver-Client-Id") clientId : String,
        @Header("X-Naver-Client-Secret") clientSecret : String,
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null
    ): Observable<ResultGetSearchMovie>

}