package com.boltztrade.app.ui.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.Articles

class ArticleListAdapter(val articlesList:MutableList<Articles> = mutableListOf(),
                         val recyclerviewSelectedPositionCallback: RecyclerviewSelectedPositionCallback) :
    RecyclerView
    .Adapter<ArticleListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.card_articles, parent, false) as CardView

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.articleTitleTextView.text = articlesList[position].title
        holder.itemView.setOnClickListener {
            recyclerviewSelectedPositionCallback.itemSelected(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val articleTitleTextView = itemView.findViewById<TextView>(R.id.article_title)
    }
}