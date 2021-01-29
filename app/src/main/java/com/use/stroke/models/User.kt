package com.use.stroke.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id : Int? = 0,
    @SerializedName("name") var name : String? = "",
    @SerializedName("email") var email : String = "",
    @SerializedName("role") var role : Int,
    @SerializedName("password") var password : String = "",
)