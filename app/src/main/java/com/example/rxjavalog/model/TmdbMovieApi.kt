package com.example.rxjavalog.model

import io.reactivex.Observable
import retrofit2.http.*

// naver -> tmdb api 변경
interface TmdbMovieApi {
    @GET("search/movie")
    fun getSearchMovie(
        @Query("api_key") apiKey : String,
        @Query("language") clientSecret : String,
        @Query("query") query: String,
        @Query("page") page: Int? = null,
        @Query("include_adult") adult: Boolean? = null
    ): Observable<ResultGetSearchMovie>

}