package com.boltztrade.app.apis

import com.boltztrade.app.model.*
import io.reactivex.Observable
import retrofit2.http.*


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

    @GET("/news/read")
    fun getNews(@Header("Authorization")authorization:String):Observable<News>

    @GET("/articles/read")
    fun getArticles(@Header("Authorization")authorization:String):Observable<MutableList<Articles>>

    @POST("/strategies/getUserStrategies")
    fun getUserStrategies(@Header("Authorization")authorization:String,@Body username:Username):Observable<MutableList<StrategyModel>>

    @POST("/playground/getInstrumentList")
    fun getInstrumentList(@Header("Authorization")authorization:String,@Body instrumentRegex:InstrumentRegex):Observable<MutableList<Instrument>>

    @POST("/strategies/create")
    fun createStrategies(@Header("Authorization")authorization:String,@Body strategy:StrategyModel):Observable<StrategyModel>

    @POST("/strategies/addAuthor")
    fun addStrategyAuthor(@Header("Authorization")authorization:String,@Body addStrategyAuthor: Strategies.AddStrategyAuthor):Observable<StrategyModel>


}