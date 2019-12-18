package com.boltztrade.app.model

import java.util.*

data class StrategyModel(var _id:String? = "",var algoName:String,var instrument:String?,val tradingSymbol:String?, var exchange:String?="nse",
                         var author:String,var status:Int = StrategyStatus.DRAFT.ordinal,var publisher:String?,
                         var position:String?,var quantity:Double?,var candleInterval:String?,
                         var entry:MutableList<Strategy> = mutableListOf(),
                         var exit:MutableList<Strategy> = mutableListOf(),
                         var targetProfitPercent:Float?,var stopLossPercent:Float?,var createdOn:Date?)

data class Strategy(var firstIndicator:Indicator,var secondIndicator:Indicator,
                    var comparisonOperator:String,var logicalOperator:String?)

data class Indicator(var name:String,var properties:MutableMap<Any?,Any?> = mutableMapOf())

enum class StrategyStatus{DRAFT,BACKTEST,DEPLOYED,ENTERED,EXITED,DEPRECATED,PUBLISHED}