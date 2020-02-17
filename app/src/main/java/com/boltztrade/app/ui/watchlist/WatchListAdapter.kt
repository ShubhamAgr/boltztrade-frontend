package com.boltztrade.app.ui.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.BrokerService
import com.boltztrade.app.model.Instrument
import java.lang.Exception

class WatchListAdapter(val userWatchlist:MutableList<Instrument> = mutableListOf(),
                       val instrumentOHLCQuote:MutableMap<String, BrokerService.OHLCQuote> = mutableMapOf(),
                       val recyclerviewSelectedPositionCallback: RecyclerviewSelectedPositionCallback) :
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
        val indicator = userWatchlist[position].instrument_token
        try {
            holder.openValue.text = instrumentOHLCQuote[indicator]?.ohlc?.open.toString()
            holder.highValue.text = instrumentOHLCQuote[indicator]?.ohlc?.high.toString()
            holder.lowValue.text = instrumentOHLCQuote[indicator]?.ohlc?.low.toString()
            holder.closeValue.text = instrumentOHLCQuote[indicator]?.ohlc?.close.toString()
            holder.ltpValue.text = instrumentOHLCQuote[indicator]?.last_price.toString()
        }catch (e:Exception){
            e.printStackTrace()
        }
        holder.itemView.setOnClickListener {
            recyclerviewSelectedPositionCallback.itemSelected(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val instrumentName = itemView.findViewById<TextView>(R.id.instrument_name)
        val openValue:TextView = itemView.findViewById(R.id.open_value)
        val highValue:TextView = itemView.findViewById(R.id.high_value)
        val lowValue:TextView = itemView.findViewById(R.id.low_value)
        val closeValue:TextView = itemView.findViewById(R.id.close_value)
        val ltpValue:TextView = itemView.findViewById(R.id.ltp_value)
    }
}