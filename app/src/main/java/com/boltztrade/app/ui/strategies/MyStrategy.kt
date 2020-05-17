package com.boltztrade.app.ui.strategies

import com.boltztrade.app.model.Instrument
import com.boltztrade.app.model.Strategy
import com.boltztrade.app.model.StrategyModel
import java.util.*

object MyStrategy {
    private  var selectedInstrument: Instrument? = null
    private  var entryStrategy:MutableList<Strategy> = mutableListOf()
    private  var exitStrategy:MutableList<Strategy> = mutableListOf()
    private var quantity :Double = 0.0
    private var algoName:String = ""
    private var targetProfitPercent:Float = 0f
    private var stopLossPercent:Float = 0f
    private var period:String="day"
    private var candleInterval:String = "minute" //minute, day, 3minute,5minute,10minute,15minute,30minute,60minute
    private var createdOnDate:String = "" //server will add create on date..

    private var position:String = "Buy"


    fun setMSelectedInstrument(instrument: Instrument){
        selectedInstrument = instrument
    }

    fun getMSelectedInstrument():Instrument?{
        return selectedInstrument
    }

    fun setMEntryCondition(entryStrategy:MutableList<Strategy>){
        this.entryStrategy = entryStrategy
    }

    fun getMEntryCondition():MutableList<Strategy>{
        return entryStrategy
    }

    fun setMExitCondition(exitStrategy:MutableList<Strategy>){
        this.exitStrategy = exitStrategy
    }

    fun getMExitCondition():MutableList<Strategy>{
        return exitStrategy
    }

    fun setMAlgoName(algoName:String){
        this.algoName = algoName
    }

    fun getMAlgoName():String{
        return algoName
    }

    fun setMTargetProfitPercent(targetProfitPercent:Float){
        this.targetProfitPercent = targetProfitPercent
    }

    fun getMTargetProfitPercent():Float{
        return targetProfitPercent
    }

    fun setMTargetLossPercent(stopLossPercent:Float){
        this.stopLossPercent = stopLossPercent
    }

    fun getMTargetLossPercent():Float{
        return stopLossPercent
    }
    fun setMQuantity(quantity:Double){
        this.quantity = quantity
    }

    fun getMQuantity():Double{
        return quantity
    }

    fun setMPeriod(period:String){
        this.period = period
    }

    fun getMPeriod():String{
        return period
    }
    fun setMCandleInterval(candleInterval:String){
        this.candleInterval = candleInterval
    }
    fun getMCandleInterval():String{
        return candleInterval
    }

    fun setMPosition(position:String){
        this.position = position
    }

    fun getMPosition():String{
        return position
    }
    fun createStrategy():StrategyModel{
        val strategy = StrategyModel(algoName = algoName,instrument = selectedInstrument?.instrument_token,tradingSymbol = selectedInstrument?.tradingsymbol,
            exchange = selectedInstrument?.exchange,author = "",publisher = "",quantity = quantity,period = period,
            candleInterval = candleInterval,entry = entryStrategy,position = position,stopLossPercent = stopLossPercent,
            targetProfitPercent = targetProfitPercent,createdOn = null,exit = exitStrategy
        )
        return strategy
    }
}