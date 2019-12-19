package com.boltztrade.app.apis

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object BoltztradeRetrofit {
    val mRetrofit = Retrofit.Builder()
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create())
        .baseUrl("http://192.168.0.4:8080")
        .build()
    private var instance = mRetrofit.create(ApiService::class.java)

    fun getInstance():ApiService{
        return this.instance
    }
}