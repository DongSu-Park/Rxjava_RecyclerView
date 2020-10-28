package com.example.rxjavalog.repository

import android.app.Application
import android.util.Log
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.model.LinkResult
import com.kakao.sdk.link.rx
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class KakaoFeedRepository(link: String, title: String, backdropPath: String) {
    private val kakaoLink = Link(link, link)
    private val content =
        Content(title, backdropPath, kakaoLink, "$title 의 내용을 TMDB Movie Finder 에서 찾아보세요!!")
    private val buttons: List<Button> = listOf(
        Button("웹으로 보기", kakaoLink),
        Button("앱으로 보기", Link(androidExecParams = mapOf(), iosExecParams = null))
    )

    private val setFeedMessage = FeedTemplate(content, null, buttons)

    fun sendFeedMessage(applicationContext: Application): Single<LinkResult> {
        return LinkClient.rx.defaultTemplate(applicationContext, setFeedMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { data ->
                Log.d("KakaoLink Call State : ", "카카오 링크 보내기 성공 ${data.intent}")
            }
            .doOnError{error ->
                Log.d("KakaoLink Call State : ", "에러 발생 $error")
            }
    }
}