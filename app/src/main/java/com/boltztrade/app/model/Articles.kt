package com.boltztrade.app.model

data class Articles(var _id: String?,var title:String,var author:String?,
                    var content:MutableMap<Any?,Any?> = mutableMapOf()
                    ,var likes:MutableMap<Any?,Any?> = mutableMapOf(),
                    val comments:MutableMap<Any?,Any?> = mutableMapOf())