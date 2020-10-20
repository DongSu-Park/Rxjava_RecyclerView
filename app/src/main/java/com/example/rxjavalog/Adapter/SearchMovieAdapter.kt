package com.example.rxjavalog.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rxjavalog.R
import com.example.rxjavalog.model.ResultGetSearchMovie
import kotlinx.android.synthetic.main.item_movie.view.*

class SearchMovieAdapter(context: Context?, movieItems: ArrayList<ResultGetSearchMovie.Items>) :
    RecyclerView.Adapter<SearchMovieAdapter.ViewHolder>() {

    private var getMovieList = ArrayList<ResultGetSearchMovie.Items>()
    private val viewContext = context

    init {
        // 필터링된 리스트 (검색된 내용, 초기에는 모든 내용 포함)
        this.getMovieList = movieItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchMovieAdapter.ViewHolder, position: Int) {
        val currentItem = getMovieList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = getMovieList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResultGetSearchMovie.Items) {
            // 텍스트뷰, 이미지 바인딩
            itemView.run {
                Glide.with(viewContext!!).load(item.image).error(R.drawable.ic_baseline_movie_24).into(iv_movie_image)
                tv_movie_title.text = item.title.replace("<b>", "").replace("</b>", "")
                tv_movie_subtitle.text = item.subtitle
                tv_movie_pubdate.text = "Date : ${item.pubDate}"
                tv_movie_director.text = "Director : ${item.director.replace("|", " ")}"
                tv_movie_actor.text = "Actor : ${item.actor.replace("|", " ")}"
                tv_movie_naverUserRating.text = "Naver UserRating : ${item.userRating}"
            }
        }
    }
}