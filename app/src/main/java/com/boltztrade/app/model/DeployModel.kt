package com.boltztrade.app.model

class DeployModel(var _id: String?, var strategyId: String?,var userId:String?,var interval:String="minute",
                  var timestamp:Long?, var orderTimestamps:MutableList<Long> = mutableListOf(),
                  var deployOrder:MutableList<DeployOrder> = mutableListOf())

data class DeployOrder(val amount:String?,val cost:String,val type:String,val netPrice:String,
                       val pricePerAsset:String)