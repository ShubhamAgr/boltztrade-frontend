package com.boltztrade.app.ui.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.Instrument

class WatchListAdapter(val userWatchlist:MutableList<Instrument> = mutableListOf(), val recyclerviewSelectedPositionCallback: RecyclerviewSelectedPositionCallback) :
    RecyclerView
    .Adapter<WatchListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.card_watchlist, parent, false) as CardView

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return userWatchlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.instrumentName.text = userWatchlist[position].name
        holder.itemView.setOnClickListener {
            recyclerviewSelectedPositionCallback.itemSelected(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val instrumentName = itemView.findViewById<TextView>(R.id.instrument_name)
    }
}