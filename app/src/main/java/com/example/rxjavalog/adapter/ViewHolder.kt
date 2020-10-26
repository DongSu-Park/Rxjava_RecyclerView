package com.example.rxjavalog.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavalog.databinding.ItemGridTitleBinding
import com.example.rxjavalog.model.ResultGetSearchMovie
import com.example.rxjavalog.ui.MovieDetailActivity

class ViewHolder(itemGridTitleBinding: ItemGridTitleBinding, context: Context) :
    RecyclerView.ViewHolder(itemGridTitleBinding.root) {
    private var binding = itemGridTitleBinding
    private val mainContext = context

    var image: String? = null
    var link: String? = null
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
        link = item.link
        title = item.title.replace("<b>", "").replace("</b>", "")
        subTitle = item.subtitle.replace("<b>", "").replace("</b>", "")
        pubDate = "개봉일 : ${item.pubDate}"
        director = "감독 : ${item.director.replace("|", ", ")}"
        director = director!!.substring(0, director!!.length-2)
        actor = "주연 : ${item.actor.replace("|", ", ")}"
        actor = actor!!.substring(0, actor!!.length-2)
        naverUserRating = item.userRating
    }

    fun imgOnClick(){
        val intent = Intent(mainContext, MovieDetailActivity::class.java)

        intent.run{
            putExtra("image", image)
            putExtra("link", link)
            putExtra("title", title)
            putExtra("subTitle", subTitle)
            putExtra("pubDate", pubDate)
            putExtra("director", director)
            putExtra("actor", actor)
            putExtra("naverUserRating", naverUserRating)
        }

        // startActivity
        mainContext.startActivity(intent)
    }
}