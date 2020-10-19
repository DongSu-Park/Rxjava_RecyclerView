package com.example.rxjavalog

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxjavalog.Adapter.StoreAdapter
import com.example.rxjavalog.model.Store
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val storeList = ArrayList<Store>()
    private lateinit var initObservable: Disposable
    private var searchObservable = CompositeDisposable()

    private var storeAdapter = StoreAdapter(storeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초기 데이터 세팅
        initStoreList()
        searchText()

    }

    @SuppressLint("CheckResult")
    private fun searchText() {
        val searchDisposable: Disposable = Observable.create<String> { emitter ->
            tv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (storeList.contains(query)) {
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
                storeAdapter.filter.filter(data)
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

    private fun initStoreList() {
        // 초기 데이터 설정 (하드코딩)
        storeList.run {
            add(Store("test", "test address", "test address street0"))
            add(Store("test1", "test address1", "test address street1"))
            add(Store("test2", "test address2", "test address street2"))
            add(Store("test3", "test address3", "test address street3"))
            add(Store("test4", "test address4", "test address street4"))
            add(Store("test5", "test address5", "test address street5"))
        }

        layout_store_recyclerView.run {
            // fromArray 로 배열리스트 불러오기
            initObservable = Observable.fromArray(storeList)
                .doOnNext { data ->
                    Log.d("TAG", "Recycler Observable : $data")
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { data ->
                    adapter = StoreAdapter(data)
                    storeAdapter = adapter as StoreAdapter
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                }
        }
    }


}