package com.example.rxjavalog.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavalog.R
import com.example.rxjavalog.model.Store
import kotlinx.android.synthetic.main.item_store.view.*

class StoreAdapter(var storeItems: ArrayList<Store>) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>(), Filterable {

    private var filteredList = ArrayList<Store>()
    private var unFilteredList = ArrayList<Store>()

    init {
        // 필터링된 리스트 (검색된 내용, 초기에는 모든 내용 포함)
        this.filteredList = storeItems
        // 필터링 안된 리스트 (모든 내용)
        this.unFilteredList = storeItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreAdapter.ViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = filteredList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Store) {
            itemView.tv_item_name.text = item.name
            itemView.tv_item_address_origin.text = item.address_origin
            itemView.tv_item_address_street.text = item.address_street
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                // list init
                filteredList = storeItems

                val charString = constraint.toString()

                if (charString.isEmpty()) {
                    filteredList = unFilteredList
                } else {
                    val filteringList = ArrayList<Store>()

                    for (storeName: Store in filteredList) {
                        if (storeName.name.toLowerCase()
                                .contains(charString.toLowerCase()) || storeName.address_origin.toLowerCase()
                                .contains(charString.toLowerCase()) || storeName.address_street.toLowerCase()
                                .contains(charString.toLowerCase())
                        ) {
                            // 검색된 리스트
                            filteringList.add(storeName)
                        }
                    }
                    filteredList = filteringList
                }

                val filterResult = FilterResults()
                filterResult.values = filteredList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Store>
                notifyDataSetChanged()
            }
        }
    }

}