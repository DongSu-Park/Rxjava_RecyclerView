package com.example.rxjavalog.model

import com.google.gson.annotations.SerializedName

// Response 응답 결과 쿼리에 대한 모델링 (NAVER -> TMDB 변경)
data class ResultGetSearchMovie(
    @SerializedName("page")
    var page: Int = 1,
    @SerializedName("total_results")
    var totalResult: Int = 0,
    @SerializedName("total_pages")
    var totalPages: Int = 0,
    @SerializedName("results")
    var results: List<Results>
) {
    data class Results(
        @SerializedName("popularity")
        var popularity: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("video")
        var video: Boolean = false,
        @SerializedName("vote_count")
        var voteCount: Int = 0,
        @SerializedName("vote_average")
        var voteAverage: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("release_date")
        var releaseDate: String = "",
        @SerializedName("original_language")
        var originalLanguage: String = "",
        @SerializedName("original_title")
        var originalTitle: String = "",
        @SerializedName("genre_ids")
        var genreIds: List<Int> = listOf(),
        @SerializedName("poster_path")
        var posterPath: String = "",
        @SerializedName("backdrop_path")
        var backdropPath: String = "",
        @SerializedName("overview")
        var overview: String = "",
        @SerializedName("adult")
        var adult: Boolean = false
    )
}

