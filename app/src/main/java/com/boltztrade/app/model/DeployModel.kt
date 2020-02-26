package com.boltztrade.app.model

class DeployModel(var _id: String?, var strategyId: String?,var userId:String?,
                  var timestamp:Long,var deployState: MutableList<DeployState> = mutableListOf(),
                  var deployOrder:MutableList<DeployOrder> = mutableListOf())

class DeployOrder(var exchange:String,var tradingSymbol:String,var currentInstrumentPrice:Double? = 0.0,var transactionType:String,
                  var quantity:Float,var product:String,var orderType:String,var validity:String)

data class DeployState(var orderId:String?,var condition:String?,var isSuccess:Boolean?)