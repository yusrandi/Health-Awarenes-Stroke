package com.use.steps.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.steps.models.BasisPengetahuan
import com.use.steps.utils.SingleLiveEvent
import com.use.steps.utils.WrappedListResponse
import com.use.steps.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasisPengetahuanViewModel : ViewModel() {
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<BasisPengetahuanState> = SingleLiveEvent()
    private var basisPengetahuans = MutableLiveData<List<BasisPengetahuan>>()

    fun fetchBasisPengetahuans() {
        state.value = BasisPengetahuanState.IsLoading(true)
        api.fetchBasisPengetahuan().enqueue(object : Callback<WrappedListResponse<BasisPengetahuan>>{
            override fun onResponse(
                call: Call<WrappedListResponse<BasisPengetahuan>>,
                response: Response<WrappedListResponse<BasisPengetahuan>>
            ) {

                state.value = BasisPengetahuanState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<BasisPengetahuan>

                    state.value = BasisPengetahuanState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        basisPengetahuans.postValue(data)
                    }
                }else{
                    state.value = BasisPengetahuanState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<BasisPengetahuan>>, t: Throwable) {
                state.value = BasisPengetahuanState.IsLoading(false)
                state.value = BasisPengetahuanState.IsError(t.message.toString())
            }

        })
    }

    fun getState() = state
    fun getBasisPengetahuan() = basisPengetahuans

}

sealed class BasisPengetahuanState {
    data class IsLoading(var state: Boolean = false) : BasisPengetahuanState()
    data class IsSuccess(var msg: String) : BasisPengetahuanState()
    data class IsError(var err: String) : BasisPengetahuanState()

}