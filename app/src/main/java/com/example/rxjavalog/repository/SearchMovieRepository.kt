package com.example.rxjavalog.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.rxjavalog.model.NaverMovieClient
import com.example.rxjavalog.model.ResultGetSearchMovie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val CLIENT_ID = ""
private const val CLIENT_SECRET = ""
private const val TAG = "Observable Tag"

class SearchMovieRepository {
    // Observable
    @SuppressLint("CheckResult")
    fun getMovieList(query : String, display : Int, start : Int) : Observable<ResultGetSearchMovie> {
        return NaverMovieClient().retrofit.getSearchMovie(CLIENT_ID, CLIENT_SECRET, query, display, start)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { data ->
                Log.d(TAG, "Retrofit Observable onNext : $data")
            }
            .doOnError { error ->
                Log.e(TAG, "Retrofit Observable Error : $error")
            }
    }
}