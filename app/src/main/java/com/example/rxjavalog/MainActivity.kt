package com.example.rxjavalog

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxjavalog.Adapter.SearchMovieAdapter
import com.example.rxjavalog.model.NaverMovieClient
import com.example.rxjavalog.model.ResultGetSearchMovie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

private const val TAG = "Tag"
private const val CLIENT_ID = "7zx2Rf8hv8MfasasLq3f"
private const val CLIENT_SECRET = "fXly52dN8o"

class MainActivity : AppCompatActivity() {

    private var searchObservable = CompositeDisposable()
    private val searchMovieList = ArrayList<ResultGetSearchMovie.Items>()
    private var searchMovieAdapter = SearchMovieAdapter(null, searchMovieList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초기 데이터 세팅
        tv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != "") {
                    Log.d(TAG, "Search Start = Query ($query)")
                    val callGetSearchMovie = NaverMovieClient().retrofit.getSearchMovie(CLIENT_ID, CLIENT_SECRET, query, 50, 1)
                    runMovieSearch(callGetSearchMovie)
                } else {
                    Toast.makeText(applicationContext, "검색어를 입력하세요", Toast.LENGTH_LONG).show()
                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun runMovieSearch(callGetSearchMovie: Observable<ResultGetSearchMovie>) {
        // 컬렉션 초기화
        searchMovieList.clear()

        // Retrofit2 + Observable 데이터 통지 시작
        val searchRetrofit = callGetSearchMovie
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { data ->
                Log.d(TAG, "Retrofit Observable onNext : $data")
            }
            .doOnError { error ->
                Log.e(TAG, "Retrofit Observable Error : $error")
            }
            .subscribe(
                // onComplete
                { data ->
                    data.items.iterator().forEach {
                        searchMovieList.add(it)
                        Log.d(TAG, "search movie data size = ${searchMovieList.size}")
                    }
                },

                // onError
                { error ->
                    Log.e(TAG, "Retrofit Error : $error")
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Unknown Error")
                        .setMessage("Unknown Error. Please retry \n\nError exception code : $error")
                        .setPositiveButton("확인") { _, _ -> }
                        .show()
                },

                // onComplete
                {
                    layout_store_recyclerView.run {
                        adapter = SearchMovieAdapter(context, searchMovieList)
                        searchMovieAdapter = adapter as SearchMovieAdapter
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter?.notifyDataSetChanged()
                    }
                }
            )
        searchObservable.add(searchRetrofit)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 메모리 누수 방지
        if (!searchObservable.isDisposed) {
            searchObservable.dispose()
        }
    }
}