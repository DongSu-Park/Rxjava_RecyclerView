package com.example.rxjavalog.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rxjavalog.model.ResultGetSearchMovie
import com.example.rxjavalog.repository.KakaoFeedRepository
import com.example.rxjavalog.repository.SearchMovieRepository
import com.kakao.sdk.link.model.LinkResult
import io.reactivex.disposables.CompositeDisposable

private const val TAG = "Observable Tag"
private const val KAKAO_TAG = "KakaoLink Single State"

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val getMovieListItems = ArrayList<ResultGetSearchMovie.Results>()
    private val searchMovieRepository = SearchMovieRepository()
    private val applicationContext = application

    var searchObservable = CompositeDisposable()
    val liveSearchedMovieList = MutableLiveData<ArrayList<ResultGetSearchMovie.Results>>()
    val liveKakaoFeedCallBack = MutableLiveData<LinkResult>()

    val showErrorAlertDialog = MutableLiveData(false)
    val showToastMessage = MutableLiveData(false)

    var errorCode : String = ""
    var searchText : String? = ""
    var searchQuery : String? = ""

    var startPage : Int = 1
    var totalPage : Int? = null

    // Subscribe
    @SuppressLint("CheckResult")
    fun setMovieList(query: String) {
        // 검색된 리스트 초기화
        if (query != ""){
            getMovieListItems.clear()

            // Observable Start
            searchQuery = query
            val getMovieListItem = searchMovieRepository.getMovieList(searchQuery!!, startPage)

            // Subscribe Start
            val setMovieListItem = getMovieListItem
                .subscribe(
                    // onNext
                    { data ->
                        totalPage = data.totalPages

                        data.results.iterator().forEach {
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

    fun setMoreMovieList(nextPage : Int){
        val getMoreMovieListItem = searchMovieRepository.getMovieList(searchQuery!!, nextPage)

        val setMoreMovieListItem = getMoreMovieListItem
            .subscribe(
                // onNext
                { data ->
                    totalPage = data.totalPages

                    data.results.iterator().forEach {
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

    fun kakaoLinkFeedIntent(link : String, title : String, backdropPath : String){
        val kakaoFeedRepository = KakaoFeedRepository(link, title, backdropPath)

        val getFeedMessage = kakaoFeedRepository.sendFeedMessage(applicationContext)
            .subscribe(
                // onSuccess
                { data ->
                    Log.d(KAKAO_TAG, "카카오 링크 보내기 성공 ${data.intent}")

                    // kakao app intent start
                    liveKakaoFeedCallBack.value = data

                    Log.w(KAKAO_TAG, "Warning Msg : ${data.warningMsg}")
                    Log.w(KAKAO_TAG, "Argument Msg : ${data.argumentMsg}")
                },

                // onError
                { error ->
                    Log.e(KAKAO_TAG, "카카오 링크 보내기 실패", error)

                    errorCode = error.toString()
                    showErrorAlertDialog.value = true
                }
            )
        searchObservable.add(getFeedMessage)
    }
}