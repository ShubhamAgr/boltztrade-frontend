package com.boltztrade.app.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boltztrade.app.model.Instrument

@Database(entities = [Instrument::class],version = 4)
abstract class BoltztradeDatabase:RoomDatabase() {
    abstract fun instrumentDao():InstrumentsDao
}