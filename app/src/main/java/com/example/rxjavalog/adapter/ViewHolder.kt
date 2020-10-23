package com.example.rxjavalog.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavalog.databinding.ItemMovieBinding
import com.example.rxjavalog.model.ResultGetSearchMovie

class ViewHolder(itemMovieBinding: ItemMovieBinding) :
    RecyclerView.ViewHolder(itemMovieBinding.root) {
    private var binding = itemMovieBinding

    var image: String? = null
    var title: String? = null
    var subTitle: String? = null
    var pubDate: String? = null
    var director: String? = null
    var actor: String? = null
    var naverUserRating: String? = null

    fun bind(item: ResultGetSearchMovie.Items) {
        binding.run {
            itemViewHolder = this@ViewHolder
        }

        // 텍스트뷰, 이미지 바인딩
        image = item.image
        title = item.title.replace("<b>", "").replace("</b>", "")
        subTitle = item.subtitle.replace("<b>", "").replace("</b>", "")
        pubDate = "개봉일 : ${item.pubDate}"
        director = "감독 : ${item.director.replace("|", " ")}"
        actor = "주연 : ${item.actor.replace("|", " ")}"
        naverUserRating = "네이버 평점 : ${item.userRating}"
    }
}