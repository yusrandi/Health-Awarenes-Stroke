package com.use.steps.models

data class QuisionerResponse(
    val id:Int? = 0,
    val user_id:Int,
    val response:String,
    val date:String,
    val user: User
)