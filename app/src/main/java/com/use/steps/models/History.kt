package com.use.steps.models

data class History(val id:Int? = 0,
                   val user_id:Int,
                   val event:String,
                   val date: String,
                   val user: User?
)