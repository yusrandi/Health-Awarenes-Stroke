package com.use.steps.viewmodels

import androidx.lifecycle.ViewModel
import com.use.steps.models.Quisioner
import com.use.steps.utils.SingleLiveEvent
import com.use.steps.utils.WrappedListResponse
import com.use.steps.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionerViewModel : ViewModel() {
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<QuestionerState> = SingleLiveEvent()

    fun fetchQuisioners() {
        state.value = QuestionerState.IsLoading(true)
        api.fetchQuisioners().enqueue(object : Callback<WrappedListResponse<Quisioner>>{
            override fun onResponse(
                call: Call<WrappedListResponse<Quisioner>>,
                response: Response<WrappedListResponse<Quisioner>>
            ) {

                state.value = QuestionerState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<Quisioner>

                    state.value = QuestionerState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        state.value = QuestionerState.IsLoad(data = data as MutableList<Quisioner>)

                    }
                }else{
                    state.value = QuestionerState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<Quisioner>>, t: Throwable) {
                state.value = QuestionerState.IsLoading(false)
                state.value = QuestionerState.IsError(t.message.toString())
            }

        })
    }

    fun getState() = state

}

sealed class QuestionerState {
    data class IsLoading(var state: Boolean = false) : QuestionerState()
    data class IsSuccess(var msg: String) : QuestionerState()
    data class IsError(var err: String) : QuestionerState()
    data class IsLoad(var data: MutableList<Quisioner>) : QuestionerState()

}