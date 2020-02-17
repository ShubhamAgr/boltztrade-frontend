package com.boltztrade.app.apis

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson



object BoltztradeRetrofit {

    var gson = GsonBuilder()
        .setLenient()
        .create()
    val mRetrofit = Retrofit.Builder()
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create())
        .baseUrl("https://server.boltztrade.com:8080")//"http://192.168.0.100:8080")//
        .build()
    private var instance = mRetrofit.create(ApiService::class.java)

    fun getInstance():ApiService{
        return this.instance
    }
}