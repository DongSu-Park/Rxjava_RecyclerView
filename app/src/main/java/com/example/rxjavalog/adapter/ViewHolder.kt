package com.example.rxjavalog.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavalog.databinding.ItemGridTitleBinding
import com.example.rxjavalog.model.ResultGetSearchMovie
import com.example.rxjavalog.ui.MovieDetailActivity

private const val imageOriginPath : String = "https://image.tmdb.org/t/p/w500"
private const val tmdbOriginPath : String = "https://www.themoviedb.org/movie/"

class ViewHolder(itemGridTitleBinding: ItemGridTitleBinding, context: Context) :
    RecyclerView.ViewHolder(itemGridTitleBinding.root) {
    private var binding = itemGridTitleBinding
    private val mainContext = context

    var voteCount: Int? = null
    var voteAverage: String? = null
    var title: String? = null
    var releaseDate: String? = null
    var originalTitle: String? = null
    var posterPath: String? = null
    var backdropPath: String? = null
    var overview: String? = null
    var link : String? = null

    fun bind(item: ResultGetSearchMovie.Results) {
        binding.run {
            itemViewHolder = this@ViewHolder
        }

        // 텍스트뷰, 이미지 바인딩
        title = item.title
        posterPath = imageOriginPath + item.posterPath
        voteCount = item.voteCount
        voteAverage = item.voteAverage
        releaseDate = item.releaseDate
        originalTitle = item.originalTitle
        backdropPath = imageOriginPath + item.backdropPath
        overview = item.overview
        link = tmdbOriginPath + item.id
    }

    fun imgOnClick(){
        val intent = Intent(mainContext, MovieDetailActivity::class.java)

        intent.run{
            putExtra("backdropPath", backdropPath)
            putExtra("title", title)
            putExtra("originalTitle", originalTitle)
            putExtra("voteCount", voteCount)
            putExtra("voteAverage", voteAverage)
            putExtra("releaseDate", releaseDate)
            putExtra("overview", overview)
            putExtra("link", link)
        }

        // startActivity
        mainContext.startActivity(intent)
    }
}