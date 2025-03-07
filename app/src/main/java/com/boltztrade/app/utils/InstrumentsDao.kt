package com.boltztrade.app.utils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.boltztrade.app.model.Instrument

@Dao
interface InstrumentsDao {
    @Query("SELECT * FROM instrument")
    fun getAll(): List<Instrument>

    @Insert
    fun insertAll(vararg sample: Instrument)

    @Query("DELETE FROM instrument")
    fun nukeTable()

    @Query("SELECT * FROM instrument WHERE tradingsymbol LIKE '%' || :search  || '%'")
    fun instrumentList(search: String): List<Instrument>

    @Query("SELECT * FROM instrument WHERE instrument_token LIKE '%' || :search  || '%'")
    fun searchInstrumentFromToken(search: String): List<Instrument>

    @Delete
    fun delete(sample: Instrument)
}