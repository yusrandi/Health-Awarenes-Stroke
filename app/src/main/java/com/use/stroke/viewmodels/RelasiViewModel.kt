package com.use.stroke.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.stroke.models.Gejala
import com.use.stroke.models.Relasi
import com.use.stroke.utils.SingleLiveEvent
import com.use.stroke.utils.WrappedListResponse
import com.use.stroke.utils.WrappedResponse
import com.use.stroke.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RelasiViewModel : ViewModel() {
    companion object{
        const val TAG = "RelasiViewModel"
    }
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<RelasiState> = SingleLiveEvent()


    fun getDoctorCount(id: Int) {
        state.value = RelasiState.IsLoading(true)
        api.relasiDoctorCount(
            id
        ).enqueue(object : Callback<WrappedResponse<Int>> {
            override fun onFailure(call: Call<WrappedResponse<Int>>, t: Throwable) {
                state.value = RelasiState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<Int>>,
                response: Response<WrappedResponse<Int>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<Int>
                    if (body.responsecode.equals("1")) {
                        state.value = RelasiState.IsSuccess(
                            body.responsedata.toString()
                        )
                    } else {
                        state.value = RelasiState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = RelasiState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = RelasiState.IsLoading(false)
            }
        })
    }
    fun getState() = state

}

sealed class RelasiState {
    data class IsLoad(var data: MutableList<Relasi>) : RelasiState()
    data class IsLoading(var state: Boolean = false) : RelasiState()
    data class IsSuccess(var msg: String) : RelasiState()
    data class IsError(var err: String) : RelasiState()
    data class ShowToast(var message: String?) : RelasiState()

    data class Validate(
        var title: String? = null,
        var desc: String? = null,
    ) : RelasiState()

    object reset : RelasiState()

}