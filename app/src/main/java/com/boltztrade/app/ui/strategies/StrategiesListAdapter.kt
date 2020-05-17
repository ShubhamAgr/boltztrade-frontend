package com.boltztrade.app.ui.strategies


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.callbacks.StrategyOpsCallback
import com.boltztrade.app.model.StrategyModel

class StrategiesListAdapter(val strategyList:MutableList<StrategyModel>,val strategyOpsCallback: StrategyOpsCallback) :
    RecyclerView
    .Adapter<StrategiesListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.card_strategies, parent, false) as ConstraintLayout

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return strategyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.strategyNameTextView.text = strategyList[position].algoName.capitalize()
        holder.strategyNameTextView.setOnClickListener {
            strategyOpsCallback.backtestResult(position)
        }
        holder.assesmentButton.setOnClickListener {
            strategyOpsCallback.backtestResult(position)
        }
        holder.editButton.setOnClickListener {
            strategyOpsCallback.edit(position)
        }

        holder.backtestButton.setOnClickListener {
            strategyOpsCallback.backtest(position)
        }

        holder.deployButton.setOnClickListener {
            strategyOpsCallback.deploy(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val strategyNameTextView = itemView.findViewById<TextView>(R.id.strategy_name)
        val editButton = itemView.findViewById<ImageButton>(R.id.edit)
        val assesmentButton = itemView.findViewById<ImageButton>(R.id.assesment)
        val backtestButton = itemView.findViewById<Button>(R.id.backtest)
        val deployButton = itemView.findViewById<Button>(R.id.deploy)
    }
}