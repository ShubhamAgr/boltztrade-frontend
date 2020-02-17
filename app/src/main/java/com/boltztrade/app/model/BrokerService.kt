package com.boltztrade.app.model

object BrokerService {
    data class KiteDataRequest(val instruments:MutableList<String>)
    data class OHLCQuote(val instrument_token:Long,val last_price:Double,val ohlc:OHLC)
    data class OHLC(val high:Double, val low:Double,val close:Double, val open:Double)
    data class LTPQuote(val instrument_token:Double, val last_price: Double)
}