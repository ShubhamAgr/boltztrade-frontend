package com.boltztrade.app.model

object FcmService {
    data class AddFCMToken(val username:String, val token:String, val device:String)
}