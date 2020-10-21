package com.example.rxjavalog.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.rxjavalog.R

object bindAdapter {
    @JvmStatic
    @BindingAdapter("bind_image")
    fun bindImage(view: ImageView, image: String) {
        Glide.with(view.context).load(image).error(R.drawable.ic_baseline_movie_24)
            .into(view)
    }
}