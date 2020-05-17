package com.boltztrade.app.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.NewsArticles

class NewsListAdapter(val newsList:MutableList<NewsArticles>,val recyclerviewSelectedPositionCallback: RecyclerviewSelectedPositionCallback) :
    RecyclerView
    .Adapter<NewsListAdapter .ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.card_news, parent, false) as ConstraintLayout

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.newsTitle.text = newsList[position].title
        holder.itemView.setOnClickListener {
            recyclerviewSelectedPositionCallback.itemSelected(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val newsTitle = itemView.findViewById<TextView>(R.id.news_title)
    }
}