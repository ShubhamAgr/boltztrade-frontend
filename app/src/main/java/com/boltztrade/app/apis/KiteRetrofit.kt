package com.boltztrade.app.apis

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object KiteRetrofit {
    var gson = GsonBuilder()
        .setLenient()
        .create()
    val mRetrofit = Retrofit.Builder()
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create())
        .baseUrl("https://api.kite.trade/")//"https://server.boltztrade.com:8080"
        .build()
    private var instance = mRetrofit.create(KiteApiService::class.java)

    fun getInstance():KiteApiService{
        return this.instance
    }
}