package com.boltztrade.app.model

data class News(val status:String?,val totalResults:Int?,val articles:MutableList<NewsArticles> = mutableListOf())
class NewsArticles(val source:NewsSource?,val author:String?,val title:String?,val description:String?,
                           val url:String?,val urlToImage:String?,val publishedAt:String?,val content:String?)
class NewsSource(val id:Any?,val name:String)