package com.boltztrade.app.model

object InstrumentFlow {
    data class AddInstrumentModel(val username:String,val instrumentToken:String)

    data class GetInstrumentModel(val instrumentToken: String)
}