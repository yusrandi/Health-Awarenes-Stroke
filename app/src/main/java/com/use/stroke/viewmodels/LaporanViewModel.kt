package com.use.stroke.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.stroke.models.Laporan
import com.use.stroke.utils.SingleLiveEvent
import com.use.stroke.utils.WrappedListResponse
import com.use.stroke.utils.WrappedResponse
import com.use.stroke.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaporanViewModel : ViewModel() {
    companion object{
        const val TAG = "LaporanViewModel"
    }
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<LaporanState> = SingleLiveEvent()
    private var Laporans = MutableLiveData<List<Laporan>>()

    fun fetchLaporans() {
        state.value = LaporanState.IsLoading(true)
        api.fetchLaporans().enqueue(object : Callback<WrappedListResponse<Laporan>> {
            override fun onResponse(
                call: Call<WrappedListResponse<Laporan>>,
                response: Response<WrappedListResponse<Laporan>>
            ) {

                Log.e(TAG, "fetchLaporans ${response.body()}")

                state.value = LaporanState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<Laporan>

                    state.value = LaporanState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        Laporans.postValue(data)
                    }
                } else {
                    state.value = LaporanState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<Laporan>>, t: Throwable) {
                state.value = LaporanState.IsLoading(false)
                state.value = LaporanState.IsError(t.message.toString())
            }

        })
    }

    fun fetchLaporansByUser(id:Int) {
        state.value = LaporanState.IsLoading(true)
        api.fetchLaporansByUser(id).enqueue(object : Callback<WrappedListResponse<Laporan>> {
            override fun onResponse(
                call: Call<WrappedListResponse<Laporan>>,
                response: Response<WrappedListResponse<Laporan>>
            ) {

                Log.e(TAG, "fetchLaporans ${response.body()}")

                state.value = LaporanState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<Laporan>

                    state.value = LaporanState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        Laporans.postValue(data)
                    }
                } else {
                    state.value = LaporanState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<Laporan>>, t: Throwable) {
                state.value = LaporanState.IsLoading(false)
                state.value = LaporanState.IsError(t.message.toString())
            }

        })
    }

    fun store(LaporanId: Int, cfs:String, result: String) {
        state.value = LaporanState.IsLoading(true)
        api.laporanStore(
            LaporanId, cfs, result
        ).enqueue(object : Callback<WrappedResponse<Laporan>> {
            override fun onFailure(call: Call<WrappedResponse<Laporan>>, t: Throwable) {
                state.value = LaporanState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<Laporan>>,
                response: Response<WrappedResponse<Laporan>>
            ) {
                Log.e(TAG, "fetchLaporans ${response}")

                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<Laporan>
                    if (body.responsecode.equals("1")) {
                        state.value = LaporanState.IsSuccess(
                            body.responsemsg.toString()
                        )
                    } else {
                        state.value = LaporanState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = LaporanState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = LaporanState.IsLoading(false)
            }
        })
    }


    fun getState() = state
    fun getLaporan() = Laporans

}

sealed class LaporanState {
    data class IsLoading(var state: Boolean = false) : LaporanState()
    data class IsSuccess(var msg: String) : LaporanState()
    data class IsError(var err: String) : LaporanState()

}