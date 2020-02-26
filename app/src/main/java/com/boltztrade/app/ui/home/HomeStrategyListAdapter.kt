package com.boltztrade.app.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.callbacks.StrategyOpsCallback
import com.boltztrade.app.model.DeployModel
import com.boltztrade.app.model.StrategyModel

class HomeStrategyListAdapter(val deployList:MutableList<HomeFragment.DeployedStrategy>, val strategyOpsCallback: StrategyOpsCallback) :
    RecyclerView
    .Adapter<HomeStrategyListAdapter.ViewHolder>() {
    private val LOG_TAG = HomeStrategyListAdapter::class.java.canonicalName
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.home_card_strategies, parent, false) as CardView

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return deployList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.strategyNameTextView.text =deployList[position].strategyName
        holder.itemView.setOnClickListener {
            Log.d(LOG_TAG,"clicked")
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val strategyNameTextView = itemView.findViewById<TextView>(R.id.strategy_name)
    }
}