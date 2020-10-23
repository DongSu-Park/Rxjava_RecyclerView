package com.example.rxjavalog.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.rxjavalog.model.NaverMovieClient
import com.example.rxjavalog.model.ResultGetSearchMovie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val CLIENT_ID = "7zx2Rf8hv8MfasasLq3f"
private const val CLIENT_SECRET = "fXly52dN8o"
private const val TAG = "Observable Tag"

class SearchMovieRepository {
    // Observable
    @SuppressLint("CheckResult")
    fun getMovieList(query : String) : Observable<ResultGetSearchMovie> {
        return NaverMovieClient().retrofit.getSearchMovie(CLIENT_ID, CLIENT_SECRET, query, 50, 1)
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