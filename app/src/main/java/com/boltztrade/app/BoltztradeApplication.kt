package com.boltztrade.app

import android.app.Application
import android.util.Log

class BoltztradeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Create application....","create application.....")
        BoltztradeSingleton.initializeDatabase(this)
        BoltztradeSingleton.initializeSharedPreferences(this)
        BoltztradeSingleton.initializeFirebase()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }
}