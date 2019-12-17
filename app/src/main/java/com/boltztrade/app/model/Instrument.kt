package com.boltztrade.app.model


data class Instrument(var _id:String?, var instrument_token:String?, var exchange_token:Int?, var tradingsymbol:String?,
                       var name:String?, var last_price:Int?, var expiry:String?, var strike:Int?, var tick_size:Double?,
                       var lot_size:Int?, var instrument_type:String?, var segment:String?, var exchange:String?)