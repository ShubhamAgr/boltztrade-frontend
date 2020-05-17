package com.boltztrade.app.ui.strategies



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.StrategyCardTouchCallback
import com.boltztrade.app.model.Strategy

class CreateStrategyListAdapter(val strategies:MutableList<Strategy> = mutableListOf(),val strategyCardTouchCallback:StrategyCardTouchCallback) :
    RecyclerView
    .Adapter<CreateStrategyListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.card_create_strategy, parent, false) as ConstraintLayout

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return strategies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.firstIndicatorButton.setOnClickListener {strategyCardTouchCallback.onSelectFirstIndicator(position)}
        holder.secondIndicatorButton.setOnClickListener { strategyCardTouchCallback.onSelectSecondIndicator(position)}
        holder.logicalOperatorButton.setOnClickListener { strategyCardTouchCallback.onSelectLogicalOperator(position)}
        holder.comparisonOperatorButton.setOnClickListener {strategyCardTouchCallback.onSelectComparisonOperator(position)}

        holder.firstIndicatorButton.text = strategies[position].firstIndicator.name
        holder.secondIndicatorButton.text = if(strategies[position].secondValue != null)strategies[position].secondValue.toString() else strategies[position].secondIndicator?.name
        holder.logicalOperatorButton.text = strategies[position].logicalOperator
        holder.comparisonOperatorButton.text = strategies[position].comparisonOperator

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val context = itemView.context
        val firstIndicatorButton = itemView.findViewById<Button>(R.id.indicator1)
        val secondIndicatorButton = itemView.findViewById<Button>(R.id.indicator2)
        val logicalOperatorButton = itemView.findViewById<Button>(R.id.logical_operator)
        val comparisonOperatorButton = itemView.findViewById<Button>(R.id.comparison_operator)

    }
}