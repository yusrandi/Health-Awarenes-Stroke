package com.use.stroke.viewmodels

import androidx.lifecycle.ViewModel
import com.use.stroke.models.Quisioner
import com.use.stroke.models.QuisionerResponse
import com.use.stroke.utils.SingleLiveEvent
import com.use.stroke.utils.WrappedListResponse
import com.use.stroke.utils.WrappedResponse
import com.use.stroke.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuisionerResponseViewModel : ViewModel() {
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<QuisionerResponseState> = SingleLiveEvent()

    fun fetchQuisionersResponse() {
        state.value = QuisionerResponseState.IsLoading(true)
        api.fetchQuisionersResponse().enqueue(object : Callback<WrappedListResponse<QuisionerResponse>>{
            override fun onResponse(
                call: Call<WrappedListResponse<QuisionerResponse>>,
                response: Response<WrappedListResponse<QuisionerResponse>>
            ) {

                state.value = QuisionerResponseState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<QuisionerResponse>

                    state.value = QuisionerResponseState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        state.value = QuisionerResponseState.IsLoad(data = data as MutableList<QuisionerResponse>)

                    }
                }else{
                    state.value = QuisionerResponseState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<QuisionerResponse>>, t: Throwable) {
                state.value = QuisionerResponseState.IsLoading(false)
                state.value = QuisionerResponseState.IsError(t.message.toString())
            }

        })
    }
    fun store(userId:Int, response:String, date:String) {
        state.value = QuisionerResponseState.IsLoading(true)
        api.quisionersResponseStore(
            userId, response, date
        ).enqueue(object : Callback<WrappedResponse<QuisionerResponse>> {
            override fun onFailure(call: Call<WrappedResponse<QuisionerResponse>>, t: Throwable) {
                state.value = QuisionerResponseState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<QuisionerResponse>>,
                response: Response<WrappedResponse<QuisionerResponse>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<QuisionerResponse>
                    if (body.responsecode.equals("1")) {
                        state.value = QuisionerResponseState.IsSuccess(
                            body.responsemsg.toString()
                        )
                    } else {
                        state.value = QuisionerResponseState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = QuisionerResponseState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = QuisionerResponseState.IsLoading(false)
            }
        })
    }

    fun getState() = state

}

sealed class QuisionerResponseState {
    data class IsLoading(var state: Boolean = false) : QuisionerResponseState()
    data class IsSuccess(var msg: String) : QuisionerResponseState()
    data class IsError(var err: String) : QuisionerResponseState()
    data class IsLoad(var data: MutableList<QuisionerResponse>) : QuisionerResponseState()
}