package com.boltztrade.app.apis

import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable


interface ApiService {

    @GET("/services/rest/")
    fun mGetMyImages(@Query("method") method: String,
                     @Query("api_key") apiKey:String,
                     @Query("text") text:String,
                     @Query("format")format:String = "json",
                     @Query("nojsoncallback")NoJsonCallback:Int=1,
                     @Query("per_page")perPage:Int = 2,
                     @Query("page")page:Int = 1):Observable<String>
}