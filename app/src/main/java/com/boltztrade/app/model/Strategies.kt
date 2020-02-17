package com.boltztrade.app.model

object Strategies {
    data class AddStrategyAuthor(val strategyId:String,val author:String)

    data class StrategyOpsRequest(val strategyId: String,val username:String)

    data class BacktestModel(var _id: String?,var userId:String?,var strategyID: String?,var timestamp:Long?=null,var largestWinningTrade:Double?=null,
                             var largestLosingTrade:Double?=null,var winningTrade:Double?=null, var losingTrade:Double?=null,
                             var winPercentage:Double?=null,var lossPercentage:Double?=null,var grossWinningTrade:Double?=null,
                             var averageWinningTrade:Double?=null,var grossLosingTrade:Double?=null,var averageLosingTrade:Double?=null,
                             var netProfit:Double?=null,var netLoss:Double?=null,var result:String?=null,var fileName:String? = "")
}