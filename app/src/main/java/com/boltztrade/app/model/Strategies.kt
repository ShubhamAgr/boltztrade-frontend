package com.boltztrade.app.model

object Strategies {
    data class AddStrategyAuthor(val strategyId:String,val author:String)

    data class StrategyOpsRequest(val strategyId: String,val username:String)

    data class DeployOpsRequest(val strategyId: String,val username:String,val interval:String)

    data class BacktestOpsRequest(val strategyId: String,val username:String,val from:String,val to:String)

    data class BacktestModel(var _id: String?,var userId:String?,var strategyID: String?,var timestamp:Long?=null,var averageProfitableTrade:String?=null,
                             var averageProfit:String?,var expectedShortFall:String?=null,var maximumDrawdown:String?=null,var breakEvenTrade:String?=null,var numOfLosingTrade:String?=null,
                             var numOfTrade:String?=null,var numOfWinningTrade:String?=null,var profitLossValue:String?=null,var rewardRiskRatio:String?=null,
                             var grossLoss:String?=null,var grossProfit:String?=null, var valueAtRisk:String?=null, var buyvsHold:String?=null)
}