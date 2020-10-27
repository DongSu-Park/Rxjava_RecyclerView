package com.example.rxjavalog.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.rxjavalog.R
import com.example.rxjavalog.databinding.ActivityMoviedetailBinding


class MovieDetailActivity : AppCompatActivity() {
    var voteCount: String? = null
    var voteAverage: String? = null
    var title: String? = null
    var releaseDate: String? = null
    var originalTitle: String? = null
    var backdropPath: String? = null
    var overview: String? = null
    var link : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMoviedetailBinding>(this, R.layout.activity_moviedetail)
        binding.detailMovie = this
        binding.lifecycleOwner = this

        val intent : Intent = intent

        backdropPath = intent.extras?.getString("backdropPath")
        title = intent.extras?.getString("title")
        originalTitle = intent.extras?.getString("originalTitle")
        voteCount = "(${intent.extras?.getInt("voteCount").toString()} count)"
        voteAverage = intent.extras?.getString("voteAverage")
        releaseDate = intent.extras?.getString("releaseDate")
        overview = intent.extras?.getString("overview")
        link = intent.extras?.getString("link")

    }

    fun tmdbMovieIntent(){
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(urlIntent)
    }
}