package com.boltztrade.app.model

object StrategyService {
    data class ProfitData(val totalProfit:Double)
    data class EntryConditionMetCount(val entryConditionMet:Int)
    data class TotalStrategyCreatedCount(val totalStrategiesCreated:Int)
    data class TotalBacktestDoneCount(val totalBacktestDone:Int)
}