package com.boltztrade.app.ui.strategies


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.StrategyModel

class StrategiesListAdapter(val strategyList:MutableList<StrategyModel>,val recyclerviewSelectedPositionCallback: RecyclerviewSelectedPositionCallback) :
    RecyclerView
    .Adapter<StrategiesListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.card_strategies, parent, false) as CardView

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return strategyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.strategyNameTextView.text = strategyList[position].algoName
        holder.itemView.setOnClickListener {
            recyclerviewSelectedPositionCallback.itemSelected(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val strategyNameTextView = itemView.findViewById<TextView>(R.id.strategy_name)
    }
}