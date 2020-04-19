package com.boltztrade.app.ui.strategies

import com.boltztrade.app.model.Instrument
import com.boltztrade.app.model.Strategy
import com.boltztrade.app.model.StrategyModel
import java.util.*

object MyStrategy {
    private lateinit var selectedInstrument: Instrument
    private lateinit var entryStrategy:MutableList<Strategy>
    private lateinit var exitStrategy:MutableList<Strategy>
    private  var quantity :Double = 0.0
    private lateinit var algoName:String
    private var targetProfitPercent:Float = 0f
    private var stopLossPercent:Float = 0f

    private var candleInterval:String = "oneDayCandle"
    private var createdOnDate:String = "" //server will add create on date..

    private var position:String = "Buy"


    fun setMSelectedInstrument(instrument: Instrument){
        selectedInstrument = instrument
    }

    fun setMEntryCondition(entryStrategy:MutableList<Strategy>){
        this.entryStrategy = entryStrategy
    }

    fun setMExitCondition(exitStrategy:MutableList<Strategy>){
        this.exitStrategy = exitStrategy
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

    fun setMCandleInterval(candleInterval:String){
        this.candleInterval = candleInterval
    }

    fun setMPosition(position:String){
        this.position = position
    }

    fun createStrategy():StrategyModel{
        val strategy = StrategyModel(algoName = algoName,instrument = selectedInstrument.instrument_token,tradingSymbol = selectedInstrument.tradingsymbol,
            exchange = selectedInstrument.exchange,author = "",publisher = "",quantity = quantity,
            candleInterval = candleInterval,entry = entryStrategy,position = position,stopLossPercent = stopLossPercent,
            targetProfitPercent = targetProfitPercent,createdOn = null
        )
        return strategy
    }
}