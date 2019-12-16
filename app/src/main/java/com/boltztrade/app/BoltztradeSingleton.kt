package com.boltztrade.app

import android.content.Context
import android.content.SharedPreferences

class BoltztradeSingleton {

    companion object {
        lateinit var mSharedPreferences: SharedPreferences
        lateinit var mSharedPreferencesEditor: SharedPreferences.Editor

        fun initializeSharedPreferences(context: Context) {
            mSharedPreferences = context.getSharedPreferences("boltztradePrefs", Context.MODE_PRIVATE)
            mSharedPreferencesEditor = mSharedPreferences.edit()
        }

    }
}