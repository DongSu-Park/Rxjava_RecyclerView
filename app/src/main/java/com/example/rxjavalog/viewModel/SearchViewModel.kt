package com.example.rxjavalog.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavalog.model.NaverMovieClient
import com.example.rxjavalog.model.ResultGetSearchMovie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val CLIENT_ID = ""
private const val CLIENT_SECRET = ""
private const val TAG = "Observable Tag"

class SearchViewModel : ViewModel() {
    private val getMovieListItems = ArrayList<ResultGetSearchMovie.Items>()
    val liveSearchedMovieList = MutableLiveData<ArrayList<ResultGetSearchMovie.Items>>()
    var searchObservable = CompositeDisposable()
    val showErrorAlertDialog = MutableLiveData(false)
    var errorCode : String = ""

    // Observable
    @SuppressLint("CheckResult")
    fun getMovieList(query: String) {
        val callGetSearchMovie =
            NaverMovieClient().retrofit.getSearchMovie(CLIENT_ID, CLIENT_SECRET, query, 50, 1)
            .subscribeOn(Schedulers.io())
            .doOnNext { data ->
                Log.d(TAG, "Retrofit Observable onNext : $data")
            }
            .doOnError { error ->
                Log.e(TAG, "Retrofit Observable Error : $error")
            }

        setMovieList(callGetSearchMovie)
    }

    // Subscribe
    @SuppressLint("CheckResult")
    fun setMovieList(callGetSearchMovie: Observable<ResultGetSearchMovie>) {
        // 검색된 리스트 초기화
        getMovieListItems.clear()

        // Subscribe
        val setMovieListItem = callGetSearchMovie
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
            // onNext
            { data ->
                data.items.iterator().forEach {
                    getMovieListItems.add(it)
                    Log.d(TAG, "search movie data size = ${getMovieListItems.size}")
                }
            },

            // onError
            { error ->
                // alertDialog 호출
                Log.e(TAG, "Retrofit Error : $error")
                errorCode = error.toString()
                showErrorAlertDialog.value = true
            },

            // onComplete (
            {
                Log.d(TAG, "Retrofit Observable onComplete")
                liveSearchedMovieList.value = getMovieListItems
            }
        )

        searchObservable.add(setMovieListItem)
    }
}