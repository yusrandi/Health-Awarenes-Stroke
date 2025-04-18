package com.use.steps.webservices

import com.use.steps.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object{
        private var retrovit : Retrofit? = null
        private var opt = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient() : Retrofit{

            return if(retrovit == null){

                retrovit = Retrofit.Builder().apply {
                    client(opt)
                    baseUrl(Constants.API)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrovit!!
            }else{
                retrovit!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)


    }
}