package com.example.mancoin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mancoin.data.NewsItem
import com.example.mancoin.databinding.ItemRecyclerNewsBinding
import com.example.mancoin.itemEvent.itemEvent

class NewsAdapter(private var data: List<NewsItem>, private val itemEvent: itemEvent) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: ItemRecyclerNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindViews(dataNews: NewsItem) {
            if (dataNews.title != null && dataNews.imageurl != null) {
                binding.txtTitleNewsMdNews.text = dataNews.title
                binding.txtDescriptionItemRecyclerNews.text = dataNews.body
                Glide.with(binding.root)
                    .load(dataNews.imageurl)
                    .transform(RoundedCorners(32))
                    .into(binding.imgItemRecyclerNews)

                binding.root.setOnClickListener {
                    itemEvent.onItemClicked(dataNews)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerNewsBinding.inflate(inflate, parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    fun updateData(dataNews: List<NewsItem>) {
        data = dataNews
        notifyDataSetChanged()
    }
}