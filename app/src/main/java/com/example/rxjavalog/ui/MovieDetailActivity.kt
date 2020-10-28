package com.example.rxjavalog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.rxjavalog.R
import com.example.rxjavalog.databinding.ActivityMoviedetailBinding
import com.example.rxjavalog.viewModel.SearchViewModel
import com.kakao.sdk.common.KakaoSdk

private const val KAKAO_API_KEY = ""

class MovieDetailActivity : AppCompatActivity() {
    private val searchViewModel: SearchViewModel by viewModels()

    var voteCount: String? = null
    var voteAverage: String? = null
    var title: String? = null
    var releaseDate: String? = null
    var originalTitle: String? = null
    var backdropPath: String? = null
    var overview: String? = null
    var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KakaoSdk.init(this, KAKAO_API_KEY)

        val binding = DataBindingUtil.setContentView<ActivityMoviedetailBinding>(
            this,
            R.layout.activity_moviedetail
        )
        binding.detailMovie = this
        binding.lifecycleOwner = this

        val intent: Intent = intent

        backdropPath = intent.extras?.getString("backdropPath")
        title = intent.extras?.getString("title")
        originalTitle = intent.extras?.getString("originalTitle")
        voteCount = "(${intent.extras?.getInt("voteCount").toString()} count)"
        voteAverage = intent.extras?.getString("voteAverage")
        releaseDate = intent.extras?.getString("releaseDate")
        overview = intent.extras?.getString("overview")
        link = intent.extras?.getString("link")

    }

    fun tmdbMovieIntent() {
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(urlIntent)
    }

    @SuppressLint("CheckResult")
    fun kakaoLinkFeed() {
        searchViewModel.kakaoLinkFeedIntent(link!!,title!!,backdropPath!!)

        searchViewModel.run{
            liveKakaoFeedCallBack.observe(this@MovieDetailActivity, Observer {
                startActivity(it.intent)
            })

            showErrorAlertDialog.observe(this@MovieDetailActivity, Observer {
                if (it == true){
                    val builder = AlertDialog.Builder(this@MovieDetailActivity)
                    builder.setTitle("Unknown Error")
                        .setMessage("Unknown Error. Please retry \n\nError exception code : ${searchViewModel.errorCode}")
                        .setPositiveButton("확인") { _, _ -> }
                        .show()

                    searchViewModel.showErrorAlertDialog.value = false
                }
            })
        }

    }
}