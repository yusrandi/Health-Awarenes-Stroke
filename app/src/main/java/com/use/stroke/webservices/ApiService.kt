package com.use.stroke.webservices

import com.use.stroke.models.*
import com.use.stroke.utils.WrappedListResponse
import com.use.stroke.utils.WrappedResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("role") role: Int,
        @Field("password") password: String,
        @Field("name_pembimbing") name_pembimbing: String,
        @Field("email_pembimbing") email_pembimbing: String,
        @Field("password_pembimbing") password_pembimbing: String,
        @Field("doctor_id") doctor_id: Int
    ): Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<WrappedResponse<User>>

    @GET("gejala")
    fun fetchGejalas(): Call<WrappedListResponse<Gejala>>

    @GET("cfuser")
    fun fetchGejalasCfUser(): Call<WrappedListResponse<GejalaCFUser>>

    @GET("users")
    fun fetchAllUser(): Call<WrappedListResponse<User>>

    @GET("gejala/showbykategori/{id}")
    fun fetchGejalaByKategori(
        @Path("id") id: Int,
    ): Call<WrappedListResponse<Gejala>>

    @GET("basispengetahuan")
    fun fetchBasisPengetahuan(): Call<WrappedListResponse<BasisPengetahuan>>

    @GET("laporans")
    fun fetchLaporans(): Call<WrappedListResponse<Laporan>>

    @GET("laporans/showbyuser/{id}")
    fun fetchLaporansByUser(@Path("id") id: Int): Call<WrappedListResponse<Laporan>>

    @FormUrlEncoded
    @POST("laporans")
    fun laporanStore(
        @Field("user_id") user_id: Int,
        @Field("cfs") cfs: String,
        @Field("result") result: String,
    ): Call<WrappedResponse<Laporan>>

    @GET("cfpakar")
    fun fetchCFPakars(): Call<WrappedListResponse<BasisPengetahuan>>

    @GET("research")
    fun fetchResearchs(): Call<WrappedListResponse<Research>>

    @FormUrlEncoded
    @POST("research")
    fun researchStore(
        @Field("title") cfs: String,
        @Field("desc") result: String,
    ): Call<WrappedResponse<Research>>

    @DELETE("research/{id}")
    fun researchDelete(
        @Path("id") id: Int,
    ): Call<WrappedResponse<Research>>

    @FormUrlEncoded
    @PUT("research/{id}")
    fun researchUpdate(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("desc") desc: String,
    ): Call<WrappedResponse<Research>>

    @FormUrlEncoded
    @POST("cfpakar")
    fun cfPakarStore(
        @Field("gejala") gejala: String,
        @Field("variabel") variabel: String,
        @Field("cfs") cfs: String,
        @Field("kategori_id") kategori_id: Int,
    ): Call<WrappedResponse<CFValue>>

    @FormUrlEncoded
    @PUT("cfpakar/{id}")
    fun cfPakarUpdate(
        @Path("id") id: Int,
        @Field("cf") cf: Double,
    ): Call<WrappedResponse<BasisPengetahuan>>

    @DELETE("cfpakar/{id}")
    fun cfPakarDelete(
        @Path("id") id: Int,
    ): Call<WrappedResponse<BasisPengetahuan>>

    @GET("histories/showbyuser/{id}")
    fun fetchHistoriesByUser(@Path("id") id: Int): Call<WrappedListResponse<History>>

    @FormUrlEncoded
    @POST("histories")
    fun historyStore(
        @Field("user_id") user_id: Int,
        @Field("event") event: String,
        @Field("date") date: String,
    ): Call<WrappedResponse<History>>

    @GET("quisioner")
    fun fetchQuisioners(): Call<WrappedListResponse<Quisioner>>

    @GET("quisionerresponse")
    fun fetchQuisionersResponse(): Call<WrappedListResponse<QuisionerResponse>>

    @FormUrlEncoded
    @POST("quisionerresponse")
    fun quisionersResponseStore(
        @Field("user_id") user_id: Int,
        @Field("response") event: String,
        @Field("date") date: String,
    ): Call<WrappedResponse<QuisionerResponse>>
}