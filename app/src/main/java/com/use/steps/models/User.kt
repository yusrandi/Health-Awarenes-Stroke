package com.use.steps.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id : Int? = 0,
    @SerializedName("name") var name : String? = "",
    @SerializedName("phone") var phone : String = "",
    @SerializedName("role") var role : Int,
    @SerializedName("password") var password : String = "",
)