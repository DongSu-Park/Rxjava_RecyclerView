package com.example.rxjavalog.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxjavalog.adapter.SearchMovieAdapter
import com.example.rxjavalog.R
import com.example.rxjavalog.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "Tag"

class MainActivity : AppCompatActivity() {
    val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchMovieAdapter = SearchMovieAdapter(this)

        layout_store_recyclerView.run {
            adapter = searchMovieAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        // 초기 데이터 세팅 (바인딩 전 메인에서는 이것만 남아야 함)
        tv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != "") {
                    Log.d(TAG, "Search Start = Query ($query)")
                    viewModel.getMovieList(query)

                    // LiveData 적용
                    viewModel.liveSearchedMovieList.observe(this@MainActivity, Observer { items ->
                        searchMovieAdapter.setItems(items)
                    })
                } else {
                    Toast.makeText(applicationContext, "검색어를 입력하세요", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        // onError 발생 시 alertDialog 호출
        viewModel.showErrorAlertDialog.observe(this, Observer {
            if(it == true){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Unknown Error")
                    .setMessage("Unknown Error. Please retry \n\nError exception code : ${viewModel.errorCode}")
                    .setPositiveButton("확인") { _, _ -> }
                    .show()

                // liveData 초기화
                viewModel.showErrorAlertDialog.value = false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!viewModel.searchObservable.isDisposed){
            viewModel.searchObservable.dispose()
        }
    }

}
