package com.use.steps.viewmodels

import androidx.lifecycle.ViewModel
import com.use.steps.models.Research
import com.use.steps.utils.SingleLiveEvent
import com.use.steps.utils.WrappedListResponse
import com.use.steps.utils.WrappedResponse
import com.use.steps.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResearchViewModel : ViewModel() {
    companion object{
        const val TAG = "ResearchViewModel"
    }
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<ResearchState> = SingleLiveEvent()

    fun fetchResearchs() {
        state.value = ResearchState.IsLoading(true)
        api.fetchResearchs().enqueue(object : Callback<WrappedListResponse<Research>> {
            override fun onResponse(
                call: Call<WrappedListResponse<Research>>,
                response: Response<WrappedListResponse<Research>>
            ) {

              state.value = ResearchState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<Research>

                    state.value = ResearchState.IsSuccess(body.responsemsg.toString(), 0)

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        state.value = ResearchState.IsLoad(data = data as MutableList<Research>)
                    }
                } else {
                    state.value = ResearchState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<Research>>, t: Throwable) {
                state.value = ResearchState.IsLoading(false)
                state.value = ResearchState.IsError(t.message.toString())
            }

        })
    }

    fun store(title:String, desc: String) {
        state.value = ResearchState.IsLoading(true)
        api.researchStore(
            title, desc
        ).enqueue(object : Callback<WrappedResponse<Research>> {
            override fun onFailure(call: Call<WrappedResponse<Research>>, t: Throwable) {
                state.value = ResearchState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<Research>>,
                response: Response<WrappedResponse<Research>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<Research>
                    if (body.responsecode.equals("1")) {
                        state.value = ResearchState.IsSuccess(
                            body.responsemsg.toString(), 1
                        )
                    } else {
                        state.value = ResearchState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = ResearchState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = ResearchState.IsLoading(false)
            }
        })
    }

    fun delete(id:Int) {
        state.value = ResearchState.IsLoading(true)
        api.researchDelete(
            id
        ).enqueue(object : Callback<WrappedResponse<Research>> {
            override fun onFailure(call: Call<WrappedResponse<Research>>, t: Throwable) {
                state.value = ResearchState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<Research>>,
                response: Response<WrappedResponse<Research>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<Research>
                    if (body.responsecode.equals("1")) {
                        state.value = ResearchState.IsSuccess(
                            body.responsemsg.toString(),2
                        )
                    } else {
                        state.value = ResearchState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = ResearchState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = ResearchState.IsLoading(false)
            }
        })
    }
    fun update(id:Int, title: String, desc: String) {
        state.value = ResearchState.IsLoading(true)
        api.researchUpdate(
            id, title, desc
        ).enqueue(object : Callback<WrappedResponse<Research>> {
            override fun onFailure(call: Call<WrappedResponse<Research>>, t: Throwable) {
                state.value = ResearchState.IsError(t.message.toString())
            }
            override fun onResponse(
                call: Call<WrappedResponse<Research>>,
                response: Response<WrappedResponse<Research>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<Research>
                    if (body.responsecode.equals("1")) {
                        state.value = ResearchState.IsSuccess(
                            body.responsemsg.toString(),2
                        )
                    } else {
                        state.value = ResearchState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = ResearchState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = ResearchState.IsLoading(false)
            }
        })
    }

    fun validate(title: String?, desc: String) : Boolean{
        state.value = ResearchState.reset

        if(title != null){

            if(title.isEmpty() ){
                state.value = ResearchState.ShowToast("Judul Tidak Boleh Kosong")
                return false
            }
            if(title.length < 5 ){
                state.value = ResearchState.Validate(title = "Judul Artikel Setidaknya 5 karakter")
                return false
            }
        }

        if(desc != null){

            if(desc.isEmpty() ){
                state.value = ResearchState.ShowToast("Deskripsi Tidak Boleh Kosong")
                return false
            }
            if(desc.length < 10 ){
                state.value = ResearchState.Validate(desc = "Deskripsi Artikel Setidaknya 10 karakter")
                return false
            }
        }


        return true
    }

    fun getState() = state

}

sealed class ResearchState {
    data class IsLoad(var data: MutableList<Research>) : ResearchState()
    data class IsLoading(var state: Boolean = false) : ResearchState()
    data class IsSuccess(var msg: String, var code:Int) : ResearchState()
    data class IsError(var err: String) : ResearchState()
    data class ShowToast(var message: String?) : ResearchState()

    data class Validate(
        var title: String? = null,
        var desc: String? = null,
    ) : ResearchState()

    object reset : ResearchState()

}