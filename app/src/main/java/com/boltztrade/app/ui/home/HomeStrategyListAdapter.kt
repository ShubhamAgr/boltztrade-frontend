package com.boltztrade.app.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.StrategyOpsCallback
import com.boltztrade.app.model.DeployModel


class HomeStrategyListAdapter(val deployList:MutableMap<String, DeployModel>, val strategyOpsCallback: StrategyOpsCallback) :
    RecyclerView
    .Adapter<HomeStrategyListAdapter.ViewHolder>() {

    val keysList = deployList.keys.toMutableList()
    private val LOG_TAG = HomeStrategyListAdapter::class.java.canonicalName
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.home_card_strategies, parent, false) as ConstraintLayout

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return keysList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.strategyNameTextView.text = keysList[position].capitalize()
        val deployModelForKey = deployList[keysList[position]]
        val candleInterval = when(deployModelForKey?.interval){
            "1minute"->{"1min"}
            "3minute"->{"3min"}
            "5minute"->{"5min"}
            "10minute"->{"10min"}
            "15minute"->{"15min"}
            else->{"1min"}
        }
        holder.intervalTextView.text = "Candle:$candleInterval"
        val deployedOrderSize = deployModelForKey?.deployOrder?.size ?: 0

        when(deployedOrderSize){
            0->{
                holder.enteredTextView.setText("Entered:pending")
                holder.exitedTextView.setText("Exited:pending")
            }
            1->{
                holder.enteredTextView.setText("Entered:₹${deployModelForKey?.deployOrder?.get(0)?.netPrice}")
                holder.exitedTextView.setText("Exited:pending")
            }
            2->{
                holder.enteredTextView.setText("Entered:₹${deployModelForKey?.deployOrder?.get(0)?.netPrice}")
                holder.exitedTextView.setText("Exited:₹${deployModelForKey?.deployOrder?.get(1)?.netPrice}")
            }
            else->{
                holder.enteredTextView.setText("Entered:₹${deployModelForKey?.deployOrder?.get(0)?.netPrice}")
                holder.exitedTextView.setText("Exited:₹${deployModelForKey?.deployOrder?.get(1)?.netPrice}")
            }
        }

        holder.itemView.setOnClickListener {
            Log.d(LOG_TAG,"clicked")
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val strategyNameTextView = itemView.findViewById<TextView>(R.id.strategy_name)
        val intervalTextView = itemView.findViewById<TextView>(R.id.interval)
        val enteredTextView = itemView.findViewById<TextView>(R.id.entered)
        val exitedTextView = itemView.findViewById<TextView>(R.id.exited)
    }
}