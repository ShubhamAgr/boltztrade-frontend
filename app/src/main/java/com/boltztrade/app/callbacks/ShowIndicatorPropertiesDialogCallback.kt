package com.boltztrade.app.callbacks

interface ShowIndicatorPropertiesDialogCallback {
    fun getProperties(properties:MutableMap<Any?,Any?>)
    fun getPrice(price:Double)
}