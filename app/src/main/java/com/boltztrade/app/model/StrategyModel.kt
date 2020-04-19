package com.boltztrade.app.model

import java.util.*

data class StrategyModel(var _id:String? = "",var algoName:String,var instrument:String?,val tradingSymbol:String?,var exchange:String?="nse",
                         var author:String?,var status:Int? = StrategyStatus.DRAFT ,var publisher:String?,var position:String?,var quantity:Double?,var candleInterval:String?,
                         var entry:MutableList<Strategy> = mutableListOf(),
                         var exit:MutableList<Strategy> = mutableListOf(),
                         var targetProfitPercent:Float?,var stopLossPercent:Float?,var createdOn: Date?)

data class Strategy(var firstIndicator :Indicator, var secondIndicator :Indicator? = null,var thirdIndicator :Indicator? = null,var secondValue:Double? = null,
                    var thirdValue:Double? = null, var comparisonOperator:String, var logicalOperator:String?)

data class Indicator(var name:String,var properties:MutableMap<Any?,Any?> = mutableMapOf())

object StrategyStatus{
    val DRAFT = 0
    val BACKTEST = 1
    val DEPLOYED = 2
    val ENTERED = 3
    val EXITED = 4
    val DEPRECATED = 5
    val PUBLISHED = 6
}