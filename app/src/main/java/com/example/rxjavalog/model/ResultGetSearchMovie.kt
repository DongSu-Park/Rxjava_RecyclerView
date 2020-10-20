package com.example.rxjavalog.model

import com.google.gson.annotations.SerializedName

// Response 응답 결과 쿼리에 대한 모델링
data class ResultGetSearchMovie(
    @SerializedName("lastBuildDate")
    var lastBuildDate : String = "",
    @SerializedName("total")
    var total : Int = 0,
    @SerializedName("start")
    var start : Int = 0,
    @SerializedName("display")
    var display : Int = 0,
    @SerializedName("items")
    var items: List<Items>

){
    data class Items(
        @SerializedName("title")
        var title : String = "",
        @SerializedName("link")
        var link : String = "",
        @SerializedName("image")
        var image : String = "",
        @SerializedName("subtitle")
        var subtitle : String = "",
        @SerializedName("pubDate")
        var pubDate : Int = 0,
        @SerializedName("director")
        var director : String = "",
        @SerializedName("actor")
        var actor : String = "",
        @SerializedName("userRating")
        var userRating : String = ""
    )
}

