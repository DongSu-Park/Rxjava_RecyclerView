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
    private val searchMovieRepository = SearchMovieRepository()
    val liveSearchedMovieList = MutableLiveData<ArrayList<ResultGetSearchMovie.Items>>()
    var searchObservable = CompositeDisposable()
    val showErrorAlertDialog = MutableLiveData(false)
    val showToastMessage = MutableLiveData(false)

    var errorCode : String = ""
    var searchText : String? = ""

    // Subscribe
    @SuppressLint("CheckResult")
    fun setMovieList(query: String) {
        // 검색된 리스트 초기화
        if (query != ""){
            getMovieListItems.clear()

            // Observable Start
            val getMovieListItem = searchMovieRepository.getMovieList(query)

            // Subscribe Start
            val setMovieListItem = getMovieListItem
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
        } else {
            showToastMessage.value = true
        }
    }
}