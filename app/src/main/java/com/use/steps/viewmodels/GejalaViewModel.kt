package com.use.steps.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.steps.models.CFValue
import com.use.steps.models.Gejala
import com.use.steps.models.GejalaCFUser
import com.use.steps.utils.SingleLiveEvent
import com.use.steps.utils.WrappedListResponse
import com.use.steps.utils.WrappedResponse
import com.use.steps.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GejalaViewModel : ViewModel() {
    companion object{
        const val TAG = "GejalaViewModel"
    }
    private var api = ApiClient.instance()
    private var state: SingleLiveEvent<GejalaState> = SingleLiveEvent()
    private var gejalas = MutableLiveData<List<Gejala>>()
    private var gejalasCFUser = MutableLiveData<List<GejalaCFUser>>()

    fun fetchGejalas() {
        state.value = GejalaState.IsLoading(true)
        api.fetchGejalas().enqueue(object : Callback<WrappedListResponse<Gejala>>{
            override fun onResponse(
                call: Call<WrappedListResponse<Gejala>>,
                response: Response<WrappedListResponse<Gejala>>
            ) {

                state.value = GejalaState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<Gejala>

                    state.value = GejalaState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        gejalas.postValue(data)
                        state.value = GejalaState.IsLoad(data as MutableList<Gejala>)

                    }
                }else{
                    state.value = GejalaState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<Gejala>>, t: Throwable) {
                state.value = GejalaState.IsLoading(false)
                state.value = GejalaState.IsError(t.toString())
            }

        })
    }
    fun fetchGejalasCFUser() {
        state.value = GejalaState.IsLoading(true)
        api.fetchGejalasCfUser().enqueue(object : Callback<WrappedListResponse<GejalaCFUser>>{
            override fun onResponse(
                call: Call<WrappedListResponse<GejalaCFUser>>,
                response: Response<WrappedListResponse<GejalaCFUser>>
            ) {

                state.value = GejalaState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<GejalaCFUser>

                    state.value = GejalaState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        gejalasCFUser.postValue(data)
                        state.value = GejalaState.IsLoadCFUser(data as MutableList<GejalaCFUser>)

                    }
                }else{
                    state.value = GejalaState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<GejalaCFUser>>, t: Throwable) {
                state.value = GejalaState.IsLoading(false)
                state.value = GejalaState.IsError(t.message.toString())
            }

        })
    }
    fun fetchGejalasByKategori(id:Int) {
        state.value = GejalaState.IsLoading(true)
        api.fetchGejalaByKategori(id).enqueue(object : Callback<WrappedListResponse<Gejala>>{
            override fun onResponse(
                call: Call<WrappedListResponse<Gejala>>,
                response: Response<WrappedListResponse<Gejala>>
            ) {

                state.value = GejalaState.IsLoading(false)

                Log.e(TAG, "Response $response")
                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<Gejala>

                    state.value = GejalaState.IsSuccess(body.responsemsg.toString())

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata
                        gejalas.postValue(data)
                        state.value = GejalaState.IsLoad(data as MutableList<Gejala>)

                    }
                }else{
                    state.value = GejalaState.IsError("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<Gejala>>, t: Throwable) {
                state.value = GejalaState.IsLoading(false)
                state.value = GejalaState.IsError(t.message.toString())
            }

        })
    }
    fun cfPakarStore(gejala:String, variabel:String, cfs:String, kategoriId:Int){
        state.value = GejalaState.IsLoading(true)
        api.cfPakarStore(
            gejala, variabel, cfs, kategoriId
        ).enqueue(object : Callback<WrappedResponse<CFValue>> {
            override fun onFailure(call: Call<WrappedResponse<CFValue>>, t: Throwable) {
                state.value = GejalaState.IsError(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<CFValue>>,
                response: Response<WrappedResponse<CFValue>>
            ) {
                Log.e(TAG, "response $response")
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<CFValue>
                    if (body.responsecode.equals("1")) {
                        state.value = GejalaState.IsSuccess(
                            body.responsemsg.toString()
                        )
                    } else {
                        state.value = GejalaState.IsError(body.responsemsg.toString())
                    }
                } else {
                    state.value = GejalaState.IsError("Gagal Menyambungkan ke server")

                }
                state.value = GejalaState.IsLoading(false)
            }
        })
    }
    fun getState() = state
    fun getGejala() = gejalas
    fun getGejalaCFUser() = gejalasCFUser

}

sealed class GejalaState {
    data class IsLoading(var state: Boolean = false) : GejalaState()
    data class IsSuccess(var msg: String) : GejalaState()
    data class IsError(var err: String) : GejalaState()
    data class IsLoad(var data: MutableList<Gejala>) : GejalaState()
    data class IsLoadCFUser(var data: MutableList<GejalaCFUser>) : GejalaState()


}