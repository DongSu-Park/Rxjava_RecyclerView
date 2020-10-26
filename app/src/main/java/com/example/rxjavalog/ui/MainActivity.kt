package com.example.rxjavalog.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rxjavalog.adapter.SearchMovieAdapter
import com.example.rxjavalog.R
import com.example.rxjavalog.databinding.ActivityMainBinding
import com.example.rxjavalog.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "Tag"

class MainActivity : AppCompatActivity() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchMovieAdapter = SearchMovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = searchViewModel

        layout_store_recyclerView.run {
            adapter = searchMovieAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            setHasFixedSize(true)
        }

        // LiveData Observer
        searchViewModel.run{
            // 검색 결과 LiveData
            liveSearchedMovieList.observe(this@MainActivity, Observer {
                if (it.size > 0){
                    searchMovieAdapter.setItems(it)
                } else {
                    Toast.makeText(this@MainActivity, "검색된 결과가 없습니다", Toast.LENGTH_LONG).show()
                }
            })

            // 검색 전 텍스트 뷰에 내용이 없을 시
            showToastMessage.observe(this@MainActivity, Observer {
                if (it == true){
                    Toast.makeText(this@MainActivity, "Please Text Input...", Toast.LENGTH_LONG).show()

                    searchViewModel.showToastMessage.value = false
                }
            })

            // onError 발생 시 alertDialog 호출
            showErrorAlertDialog.observe(this@MainActivity, Observer {
                if (it == true) {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Unknown Error")
                        .setMessage("Unknown Error. Please retry \n\nError exception code : ${searchViewModel.errorCode}")
                        .setPositiveButton("확인") { _, _ -> }
                        .show()

                    // liveData 초기화
                    searchViewModel.showErrorAlertDialog.value = false
                }
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (!searchViewModel.searchObservable.isDisposed) {
            searchViewModel.searchObservable.dispose()
        }
    }

}
