package com.boltztrade.app.ui.strategies

import com.boltztrade.app.model.Instrument
import com.boltztrade.app.model.Strategy
import com.boltztrade.app.model.StrategyModel
import java.util.*

object MyStrategy {
    private lateinit var selectedInstrument: Instrument
    private lateinit var entryStrategy:MutableList<Strategy>
    private  var quantity :Double = 0.0
    private lateinit var algoName:String
    private var targetProfitPercent:Float = 0f
    private var stopLossPercent:Float = 0f


    fun setMSelectedInstrument(instrument: Instrument){
        selectedInstrument = instrument
    }

    fun setMEntryCondition(entryStrategy:MutableList<Strategy>){
        this.entryStrategy = entryStrategy
    }

    fun setMAlgoName(algoName:String){
        this.algoName = algoName
    }

    fun setMTargetProfitPercent(targetProfitPercent:Float){
        this.targetProfitPercent = targetProfitPercent
    }

    fun setMTargetLossPercent(stopLossPercent:Float){
        this.stopLossPercent = stopLossPercent
    }

    fun setMQuantity(quantity:Double){
        this.quantity = quantity
    }

    fun createStrategy():StrategyModel{
        val strategy = StrategyModel(algoName,instrument = selectedInstrument.name,tradingSymbol = selectedInstrument.tradingsymbol,
            exchange = selectedInstrument.exchange,author = "shubham",publisher = "shubham",quantity = quantity,
            candleInterval ="1 Hour",entry = entryStrategy,position = "Buy",stopLossPercent = stopLossPercent,
            targetProfitPercent = targetProfitPercent,createdOn = Calendar.getInstance().time
        )
        return strategy
    }
}