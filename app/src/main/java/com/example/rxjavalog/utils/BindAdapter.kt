package com.example.rxjavalog.utils

import android.widget.ImageView
import android.widget.RatingBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.rxjavalog.R
import com.google.android.material.shape.RoundedCornerTreatment

object BindAdapter {
    @JvmStatic
    @BindingAdapter("bind_image")
    fun bindImage(view: ImageView, image: String) {
        Glide.with(view.context).load(image).error(R.drawable.ic_baseline_movie_24)
            .fitCenter()
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("bind_rating")
    fun bindRating(view : RatingBar, naverUserRating : String){
        view.rating = naverUserRating.toString().toFloat() / 2
    }
}