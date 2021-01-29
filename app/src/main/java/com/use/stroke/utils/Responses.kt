package com.use.stroke.utils

import com.google.gson.annotations.SerializedName

data class WrappedResponse<T>(
    @SerializedName("responsecode") var responsecode : String? = null,
    @SerializedName("responsemsg") var responsemsg : String? = null,
    @SerializedName("responsedata") var responsedata : T? = null
)

data class WrappedListResponse<T>(
    @SerializedName("responsecode") var responsecode : String? = null,
    @SerializedName("responsemsg") var responsemsg : String? = null,
    @SerializedName("responsedata") var responsedata : List<T>? = null
)