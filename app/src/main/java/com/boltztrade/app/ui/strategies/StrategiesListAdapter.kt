package com.boltztrade.app.ui.strategies


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.R

class StrategiesListAdapter() :
    RecyclerView
    .Adapter<StrategiesListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardview = LayoutInflater.from(parent?.context)
            .inflate(R.layout.fragment_home, parent, false) as CardView

        return ViewHolder(cardview)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    }
}