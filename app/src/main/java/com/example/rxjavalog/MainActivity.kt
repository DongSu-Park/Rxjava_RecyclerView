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
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

private const val TAG = "Tag"

class MainActivity : AppCompatActivity() {

    private lateinit var initObservable: Disposable
    private var searchObservable = CompositeDisposable()
    private val searchMovieList = ArrayList<ResultGetSearchMovie.Items>()
    private var searchMovieAdapter = SearchMovieAdapter(null, searchMovieList)
    private val naverMovieClient = NaverMovieClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초기 데이터 세팅
        searchText()
        runRetrofit()

    }

    @SuppressLint("CheckResult")
    private fun runRetrofit() {
        naverMovieClient.callGetSearchMovie
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Unknown Error")
                        .setMessage("Unknown Error. Please retry \n\nError exception code : $error")
                        .setPositiveButton("확인") { _, _ -> }
                        .show()
                },

                // onComplete
                {
                    layout_store_recyclerView.run{
                        adapter = SearchMovieAdapter(context, searchMovieList)
                        searchMovieAdapter = adapter as SearchMovieAdapter
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                    }
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun searchText() {
        val searchDisposable: Disposable = Observable.create<String> { emitter ->
            tv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (searchMovieList.contains(query)) {
                        emitter.onNext(query)
                    } else {
                        Toast.makeText(this@MainActivity, "No Match Found", Toast.LENGTH_LONG)
                            .show()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    emitter.onNext(newText)
                    return false
                }
            })
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                searchMovieAdapter.filter.filter(data)
            }

        searchObservable.add(searchDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Rx 메모리 누수 방지

        if (!initObservable.isDisposed || !searchObservable.isDisposed) {
            initObservable.dispose()
            searchObservable.dispose()
        }
    }
}