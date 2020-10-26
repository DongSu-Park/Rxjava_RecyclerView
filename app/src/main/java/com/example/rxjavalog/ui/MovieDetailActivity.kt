package com.example.rxjavalog.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.rxjavalog.R
import com.example.rxjavalog.databinding.ActivityMoviedetailBinding

class MovieDetailActivity : AppCompatActivity() {
    var image : String? = null
    var link : String? = null
    var title : String? = null
    var subtitle : String? = null
    var pubDate : String? = null
    var director: String? = null
    var actor: String? = null
    var naverUserRating: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMoviedetailBinding>(this, R.layout.activity_moviedetail)
        binding.detailMovie = this
        binding.lifecycleOwner = this

        val intent : Intent = intent

        image = intent.extras?.getString("image")
        link = intent.extras?.getString("link")
        title = intent.extras?.getString("title")
        subtitle = intent.extras?.getString("subTitle")
        pubDate  = intent.extras?.getString("pubDate")
        director = intent.extras?.getString("director")
        actor = intent.extras?.getString("actor")
        naverUserRating = intent.extras?.getString("naverUserRating")
    }

    fun naverMovieIntent(){
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(urlIntent)
    }
}