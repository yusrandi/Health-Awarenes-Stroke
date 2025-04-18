package com.use.steps.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.steps.models.History
import com.use.steps.utils.SingleLiveEvent
import com.use.steps.utils.WrappedListResponse
import com.use.steps.utils.WrappedResponse
import com.use.steps.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    companion object{
        const val TAG = "HistoryViewModel"
    }
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<HistoryState> = SingleLiveEvent()
    private var historys = MutableLiveData<List<History>>()


    fun fetchHistorysByUser(id:String) {
        state.value = HistoryState.IsLoading(true)
        api.fetchHistoriesByUser(id).enqueue(object : Callback<WrappedListResponse<History>> {
            override fun onResponse(
                call: Call<WrappedListResponse<History>>,
                response: Response<WrappedListResponse<History>>
            ) {

                Log.e(TAG, "fetchHistorys ${response.body()}")

                state.value = HistoryState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<History>

                    state.value = HistoryState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        state.value = HistoryState.IsLoad(data as MutableList<History>)

                    }
                } else {
                    state.value = HistoryState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<History>>, t: Throwable) {
                state.value = HistoryState.IsLoading(false)
                state.value = HistoryState.IsError(t.message.toString())
            }

        })
    }

    fun store(userId: Int, event:String, date: String) {
        state.value = HistoryState.IsLoading(true)
        api.historyStore(
            userId, event, date
        ).enqueue(object : Callback<WrappedResponse<History>> {
            override fun onFailure(call: Call<WrappedResponse<History>>, t: Throwable) {
                state.value = HistoryState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<History>>,
                response: Response<WrappedResponse<History>>
            ) {
                Log.e(TAG, "fetchHistorys $response")

                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<History>
                    if (body.responsecode.equals("1")) {
                        state.value = HistoryState.IsSuccess(
                            body.responsemsg.toString()
                        )
                    } else {
                        state.value = HistoryState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = HistoryState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = HistoryState.IsLoading(false)
            }
        })
    }


    fun getState() = state

}

sealed class HistoryState {
    data class IsLoading(var state: Boolean = false) : HistoryState()
    data class IsSuccess(var msg: String) : HistoryState()
    data class IsError(var err: String) : HistoryState()
    data class IsLoad(var data: MutableList<History>) : HistoryState()


}