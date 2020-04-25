package com.boltztrade.app.apis

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header

import retrofit2.http.Streaming

interface KiteApiService {
    @Streaming
    @GET("instruments")
    fun getInstruments(@Header("Cookie") header: String?): Observable<ResponseBody>
}