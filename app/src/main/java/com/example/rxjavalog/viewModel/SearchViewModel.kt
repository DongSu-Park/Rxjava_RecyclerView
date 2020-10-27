package com.example.rxjavalog.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavalog.model.ResultGetSearchMovie
import com.example.rxjavalog.repository.SearchMovieRepository
import io.reactivex.disposables.CompositeDisposable

private const val TAG = "Observable Tag"

class SearchViewModel : ViewModel() {
    private val getMovieListItems = ArrayList<ResultGetSearchMovie.Items>()
    private val getTotalListItems = ArrayList<ResultGetSearchMovie>()
    private val searchMovieRepository = SearchMovieRepository()
    var searchObservable = CompositeDisposable()
    val liveSearchedMovieList = MutableLiveData<ArrayList<ResultGetSearchMovie.Items>>()
    val showErrorAlertDialog = MutableLiveData(false)
    val showToastMessage = MutableLiveData(false)

    var errorCode : String = ""
    var searchText : String? = ""
    var searchQuery : String? = ""

    private val display : Int = 10
    var start : Int = 1
    var totalSize : Int? = null

    // Subscribe
    @SuppressLint("CheckResult")
    fun setMovieList(query: String) {
        // 검색된 리스트 초기화
        if (query != ""){
            getMovieListItems.clear()

            // Observable Start
            searchQuery = query
            val getMovieListItem = searchMovieRepository.getMovieList(searchQuery!!, display, start)

            // Subscribe Start
            val setMovieListItem = getMovieListItem
                .subscribe(
                    // onNext
                    { data ->
                        totalSize = data.total

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
        } else {
            showToastMessage.value = true
        }
    }

    fun setMoreMovieList(startCount : Int){
        val getMoreMovieListItem = searchMovieRepository.getMovieList(searchQuery!!, display, startCount)

        val setMoreMovieListItem = getMoreMovieListItem
            .subscribe(
                // onNext
                { data ->
                    totalSize = data.total

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

        searchObservable.add(setMoreMovieListItem)
    }
}