package com.example.rxjavalog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.rxjavalog.R
import com.example.rxjavalog.databinding.ActivityMoviedetailBinding
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.rx
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val TAG = "Kakao Link Log"
private const val KAKAO_API_KEY = ""

class MovieDetailActivity : AppCompatActivity() {
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
        // 버튼 null 부분은 앱 링크 활성화시 적용
        val kakaoLink = Link(link!!, link!!)

        val content = Content(
            title!!,
            backdropPath!!,
            kakaoLink,
            "$title 의 내용을 TMDB Movie Finder 에서 찾아보세요!!"
        )
        val buttons: List<Button> =
            listOf(Button("웹으로 보기", kakaoLink), Button("앱으로 보기", Link(androidExecParams = mapOf(), iosExecParams = null)))

        val feedMessage = FeedTemplate(content, null, buttons)

        // 해당 로직 테스트 완료 후 Repository, ViewModel 로 변경 (context 관련은 확인해야함)
        // 해당 Observable 은 Single
        val kakaoLinkObservable = LinkClient.rx.defaultTemplate(this, feedMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        kakaoLinkObservable.subscribe(
            // onSuccess
            { data ->
                Log.d(TAG, "카카오 링크 보내기 성공 ${data.intent}")
                startActivity(data.intent)

                Log.w(TAG, "Warning Msg : ${data.warningMsg}")
                Log.w(TAG, "Argument Msg : ${data.argumentMsg}")
            },

            // onError
            { error ->
                Log.e(TAG, "카카오 링크 보내기 실패", error)

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Unknown Error")
                    .setMessage("Unknown Error. Please retry \n\nError exception code : ${error.message}")
                    .setPositiveButton("확인") { _, _ -> }
                    .show()
            }
        )
    }
}