package com.use.steps.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.steps.models.BasisPengetahuan
import com.use.steps.utils.SingleLiveEvent
import com.use.steps.utils.WrappedListResponse
import com.use.steps.utils.WrappedResponse
import com.use.steps.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CFPakarViewModel : ViewModel() {
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<CFPakarState> = SingleLiveEvent()
    private var cfpakars = MutableLiveData<List<BasisPengetahuan>>()

    fun fetchCFPakars() {
        state.value = CFPakarState.IsLoading(true)
        api.fetchCFPakars().enqueue(object : Callback<WrappedListResponse<BasisPengetahuan>>{
            override fun onResponse(
                call: Call<WrappedListResponse<BasisPengetahuan>>,
                response: Response<WrappedListResponse<BasisPengetahuan>>
            ) {

                state.value = CFPakarState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<BasisPengetahuan>


                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        cfpakars.postValue(data)

                        state.value = CFPakarState.IsLoad(data as MutableList<BasisPengetahuan>)

                    }
                }else{
                    state.value = CFPakarState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<BasisPengetahuan>>, t: Throwable) {
                state.value = CFPakarState.IsLoading(false)
                state.value = CFPakarState.IsError(t.message.toString())
            }

        })
    }
    fun update(id:Int, cf:Double){
        state.value = CFPakarState.IsLoading(true)
        api.cfPakarUpdate(
            id, cf
        ).enqueue(object : Callback<WrappedResponse<BasisPengetahuan>> {
            override fun onFailure(call: Call<WrappedResponse<BasisPengetahuan>>, t: Throwable) {
                state.value = CFPakarState.IsError(t.message.toString())
            }
            override fun onResponse(
                call: Call<WrappedResponse<BasisPengetahuan>>,
                response: Response<WrappedResponse<BasisPengetahuan>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<BasisPengetahuan>
                    if (body.responsecode.equals("1")) {
                        state.value = CFPakarState.IsSuccess(
                            body.responsemsg.toString()
                        )
                    } else {
                        state.value = CFPakarState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = CFPakarState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = CFPakarState.IsLoading(false)
            }
        })
    }

    fun delete(id: Int) {
        state.value = CFPakarState.IsLoading(true)
        api.cfPakarDelete(
            id
        ).enqueue(object : Callback<WrappedResponse<BasisPengetahuan>> {
            override fun onFailure(call: Call<WrappedResponse<BasisPengetahuan>>, t: Throwable) {
                state.value = CFPakarState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<BasisPengetahuan>>,
                response: Response<WrappedResponse<BasisPengetahuan>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<BasisPengetahuan>
                    if (body.responsecode.equals("1")) {
                        state.value = CFPakarState.IsSuccess(
                            body.responsemsg.toString()
                        )
                    } else {
                        state.value = CFPakarState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = CFPakarState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = CFPakarState.IsLoading(false)
            }
        })
    }
    fun getState() = state
    fun getCFPakar() = cfpakars

}

sealed class CFPakarState {
    data class IsLoading(var state: Boolean = false) : CFPakarState()
    data class IsSuccess(var msg: String) : CFPakarState()
    data class IsLoad(var data: MutableList<BasisPengetahuan>) : CFPakarState()
    data class IsError(var err: String) : CFPakarState()

}