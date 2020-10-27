package com.example.rxjavalog.utils

import android.widget.ImageView
import android.widget.RatingBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.rxjavalog.R

object BindAdapter {
    @JvmStatic
    @BindingAdapter("bind_poster_image")
    fun bindPosterImage(view: ImageView, image: String) {
        Glide.with(view.context).load(image).error(R.drawable.ic_baseline_movie_24)
            .fitCenter()
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("bind_backdrop_image")
    fun bindBackdropImage(view: ImageView, image: String){
        Glide.with(view.context).load(image).error(R.drawable.tmdb_logo)
            .fitCenter()
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("bind_rating")
    fun bindRating(view : RatingBar, tmdbUserRating : String){
        view.rating = tmdbUserRating.toString().toFloat() / 2
    }
}