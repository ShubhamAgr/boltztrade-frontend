package com.boltztrade.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Instrument(@PrimaryKey var instrument_token:String, var exchange_token:Int?, var tradingsymbol:String?,
                      var name:String?, var last_price:Int?, var expiry:String?, var strike:Double?, var tick_size:Double?,
                      var lot_size:Int?, var instrument_type:String?, var segment:String?, var exchange:String?)


//541011974,2113328,EURINR20APR73.5000CE,"EURINR",0,2020-04-28,73.5,0.0025,1,CE,BCD-OPT,BCD