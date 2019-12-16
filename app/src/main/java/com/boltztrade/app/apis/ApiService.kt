package com.boltztrade.app.apis

import com.boltztrade.app.model.GoogleLoginResponse
import com.boltztrade.app.model.Tokens
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @GET("/services/rest/")
    fun mGetMyImages(@Query("method") method: String,
                     @Query("api_key") apiKey:String,
                     @Query("text") text:String,
                     @Query("format")format:String = "json",
                     @Query("nojsoncallback")NoJsonCallback:Int=1,
                     @Query("per_page")perPage:Int = 2,
                     @Query("page")page:Int = 1):Observable<String>


    @POST("/onBoarding/loginWithGmail")
    fun loginWithGmail(@Body tokens: Tokens):Observable<GoogleLoginResponse>
}