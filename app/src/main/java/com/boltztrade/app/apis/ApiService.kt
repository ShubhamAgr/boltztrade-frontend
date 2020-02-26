package com.boltztrade.app.apis

import com.boltztrade.app.model.*
import io.reactivex.Observable
import retrofit2.http.*


interface ApiService {

    data class Status(val status:String)
    data class MUsername(val username:String)
    data class RequestWithTimeStamp(val username: String, val tradingHourZoneTimestamp:Long)
    data class StrategyIdBody(val strategyId:String)

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

    @POST("/strategies/getStrategyFromId")
    fun getStrategyFromId(@Header("Authorization")authorization:String,@Body strategyIdBody: StrategyIdBody):Observable<StrategyModel>?

    @POST("/playground/getInstrumentList")
    fun getInstrumentList(@Header("Authorization")authorization:String,@Body instrumentRegex:InstrumentRegex):Observable<MutableList<Instrument>>

    @POST("/playground/getInstrumentToWatchListFirst")
    fun getUserWatchList(@Header("Authorization")authorization:String,@Body username: Username):Observable<MutableList<String>>

    @POST("/playground/getInstrumentFromInstrumentToken")
    fun getInstrumentToken(@Header("Authorization")authorization:String,@Body getInstrumentModel: InstrumentFlow.GetInstrumentModel):Observable<MutableList<Instrument>>


    @POST("/playground/addInstrumentToWatchListFirst")
    fun addToUserWatchList(@Header("Authorization")authorization:String,@Body addInstrumentModel: InstrumentFlow.AddInstrumentModel):Observable<String>


    @POST("/playground/removeInstrumentToWatchListFirst")
    fun removeInstrumentFromWatchList(@Header("Authorization")authorization:String,@Body addInstrumentModel: InstrumentFlow.AddInstrumentModel):Observable<Status>
    @POST("/strategies/create")
    fun createStrategies(@Header("Authorization")authorization:String,@Body strategy:StrategyModel):Observable<StrategyModel>

    @POST("/strategies/addAuthor")
    fun addStrategyAuthor(@Header("Authorization")authorization:String,@Body addStrategyAuthor: Strategies.AddStrategyAuthor):Observable<StrategyModel>


    @POST("/strategies/backtest")
    fun backtest(@Header("Authorization")authorization:String,@Body strategyOpsRequest: Strategies.StrategyOpsRequest):Observable<String>


    @POST("/strategies/deploy")
    fun deploy(@Header("Authorization")authorization:String,@Body strategyOpsRequest: Strategies.StrategyOpsRequest):Observable<String>

    @POST("/strategies/backtestResult")
    fun getBacktestResult(@Header("Authorization")authorization:String,@Body strategyOpsRequest: Strategies.StrategyOpsRequest):Observable<Strategies.BacktestModel>



    @POST("/brokerService/getOhlcData")
    fun getOhlcData(@Header("Authorization")authorization:String,@Body kiteDataRequest: BrokerService.KiteDataRequest):Observable<MutableMap<String,BrokerService.OHLCQuote>>

    @POST("/brokerService/getLtp")
    fun getLTP(@Header("Authorization")authorization:String,@Body kiteDataRequest: BrokerService.KiteDataRequest):Observable<MutableList<BrokerService.LTPQuote>>

    @POST("/fcm/addToken")
    fun addFcmToken(@Header("Authorization")authorization:String,@Body addFCMToken: FcmService.AddFCMToken):Observable<Status>


    @POST("/strategies/delete")
    fun deleteStrategy(@Header("Authorization")authorization:String,@Body strategyOpsRequest: Strategies.StrategyOpsRequest):Observable<String>



    @POST("/management/updateUserDetail")
    fun updateUserDetail(@Header("Authorization")authorization:String,@Body updateUserDetailsData: UpdateUserDetailsData):Observable<BoltztradeUserDetail>


    @POST("/management/getUserDetail")
    fun getUserDetail(@Header("Authorization")authorization:String,@Body mUsername: MUsername):Observable<BoltztradeUserDetail>


    /**strategy related new apis*/
    @POST("/strategies/getTotalStrategyCreated")
    fun getTotalStrategyCreatedCount(@Header("Authorization")authorization:String,@Body mUsername: MUsername):Observable<StrategyService.TotalStrategyCreatedCount>

    @POST("/strategies/getTotalStrategyBacktest")
    fun getTotalStrategyBacktestCount(@Header("Authorization")authorization:String,@Body mUsername: MUsername):Observable<StrategyService.TotalBacktestDoneCount>

    @POST("/strategies/getDeployedStrategies")
    fun getDeployedStrategies(@Header("Authorization")authorization:String,@Body requestWithTimeStamp: RequestWithTimeStamp):Observable<MutableList<DeployModel>>

    @POST("/strategies/getTotalEntryConditionMet")
    fun getTotalEntryConditionMetCount(@Header("Authorization")authorization:String,@Body mUsername: Username):Observable<StrategyService.EntryConditionMetCount>

    @POST("/strategies/getTodaysProfit")
    fun getTodaysProfit(@Header("Authorization")authorization:String,@Body requestWithTimeStamp: RequestWithTimeStamp):Observable<StrategyService.ProfitData>

    @POST("/strategies/getTotalProfitEarned")
    fun getTotalProfitEarned(@Header("Authorization")authorization:String,@Body mUsername: Username):Observable<StrategyService.ProfitData>

}