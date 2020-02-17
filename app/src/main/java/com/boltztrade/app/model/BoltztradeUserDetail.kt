package com.boltztrade.app.model


data class BoltztradeUserDetail(var fullName:String?="",var DateOfBirth:String?="",var age:Int?=0,
                                var tradingExperience:String? = "", var workingStatus:String? = "",
                                var lookingForTradingJob:Boolean? = false, var lookingForClient:Boolean? = false,
                                var IsSebiRegisteredTrader:Boolean? = false, var IsSebiRegisteredAdvisor:Boolean? = false,
                                var IsSebiRegisteredAnalyst:Boolean? = false, var sebiTraderRegistrationNumber:String? = "",
                                var sebiRegisteredAdvisorNumber:String? ="", var sebiRegisteredAnalystNumber:String? = "")