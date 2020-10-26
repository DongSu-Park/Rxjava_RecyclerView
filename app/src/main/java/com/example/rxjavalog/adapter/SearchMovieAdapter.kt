package com.example.rxjavalog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavalog.databinding.ItemGridTitleBinding
import com.example.rxjavalog.model.ResultGetSearchMovie

class SearchMovieAdapter() : RecyclerView.Adapter<ViewHolder>() {
    private var getSearchedMovieList = ArrayList<ResultGetSearchMovie.Items>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGridTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mainContext = parent.context

        return ViewHolder(binding, mainContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getSearchedMovieList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = getSearchedMovieList.size

    fun setItems(items : ArrayList<ResultGetSearchMovie.Items>){
        this.getSearchedMovieList = items
        notifyDataSetChanged()
    }
}