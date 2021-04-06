package com.use.stroke.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.use.stroke.models.User
import com.use.stroke.utils.SingleLiveEvent
import com.use.stroke.utils.WrappedListResponse
import com.use.stroke.utils.WrappedResponse
import com.use.stroke.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    companion object {
        const val TAG = "UserViewModel"
    }

    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchUserById(id: String) {
        state.value = UserState.IsLoading(true)

        api.fetchUserById(id).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                Log.d("UserViewModel", "onFailure ${t.message}")

                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful) {
                    Log.d("UserViewModel", "onResponse " + response.body())

                    val body = response.body() as WrappedResponse<User>

                    if (body.responsecode.equals("1")) {
                        state.value = body.responsedata?.let { UserState.IsLoadUserById(it) }
                    } else {
                        state.value = UserState.Error(body.responsemsg.toString())
                    }
                } else {
                    Log.d("UserViewModel Else ", "onResponse $response")

                    state.value = UserState.Error("Gagal Menyambungkan ke server")

                }
                state.value = UserState.IsLoading(false)
            }

        })

    }
    fun update(user: User) {
        state.value = UserState.IsLoading(true)
        api.userUpdate(user.id!!, user.name!!, user.phone, user.password)
            .enqueue(object : Callback<WrappedResponse<User>> {
                override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                    Log.d("UserViewModel", "onFailure ${t.message}")

                    state.value = UserState.Error(t.message)
                }

                override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                    if (response.isSuccessful) {
                        Log.d("UserViewModel", "onResponse " + response.body())

                        val body = response.body() as WrappedResponse<User>

                        if (body.responsecode.equals("1")) {
                            body.responsedata?.let {
                                state.value = UserState.Success(it.id!!, it.name!!, it.phone, it.role)
                            }

                        } else {
                            state.value = UserState.Error(body.responsemsg.toString())
                        }
                    } else {
                        Log.d("UserViewModel Else ", "onResponse $response")

                        state.value = UserState.Error("Gagal Menyambungkan ke server")

                    }
                    state.value = UserState.IsLoading(false)
                }

            })

    }

    fun login(email: String, password: String) {
        state.value = UserState.IsLoading(true)

        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                Log.d("UserViewModel", "onFailure ${t.message}")

                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful) {
                    Log.d("UserViewModel", "onResponse " + response.body())

                    val body = response.body() as WrappedResponse<User>

                    if (body.responsecode.equals("1")) {
                        state.value = UserState.Success(
                            body.responsedata!!.id!!, body.responsedata!!.name!!,
                            body.responsedata!!.phone,
                            body.responsedata!!.role
                        )
                    } else {
                        state.value = UserState.Error(body.responsemsg.toString())
                    }
                } else {
                    Log.d("UserViewModel Else ", "onResponse $response")

                    state.value = UserState.Error("Gagal Menyambungkan ke server")

                }
                state.value = UserState.IsLoading(false)
            }

        })

    }
    fun register(user: User, doctorId: Int) {
        state.value = UserState.IsLoading(true)
        api.register(
            user.name!!,
            user.phone,
            user.role,
            user.password,
            doctorId
        ).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                Log.d(TAG, "Register onResponse $response")
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<User>
                    if (body.responsecode.equals("1")) {
                        state.value = UserState.Success(
                            body.responsedata!!.id!!, body.responsedata!!.name!!,
                            body.responsedata!!.phone,
                            body.responsedata!!.role
                        )
                    } else {
                        state.value = UserState.Error(body.responsemsg.toString())
                    }
                } else {
                    state.value = UserState.Error("Gagal Menyambungkan ke server")

                }
                state.value = UserState.IsLoading(false)
            }
        })
    }

    fun fetchAllUser() {
        state.value = UserState.IsLoading(true)
        api.fetchAllUser().enqueue(object : Callback<WrappedListResponse<User>> {
            override fun onResponse(
                call: Call<WrappedListResponse<User>>,
                response: Response<WrappedListResponse<User>>
            ) {

                state.value = UserState.IsLoading(false)

                if (response.isSuccessful) {

                    val body = response.body() as WrappedListResponse<User>

                    if (body.responsecode.equals("1")) {
                        val data = body.responsedata

                        state.value = UserState.IsLoad(data as MutableList<User>)

                    }
                } else {
                    state.value = UserState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<User>>, t: Throwable) {
                state.value = UserState.IsLoading(false)
                state.value = UserState.Error(t.toString())
            }

        })
    }
    fun getState() = state

}

sealed class UserState {
    data class Error(var err: String?) : UserState()
    data class ShowToast(var message: String?) : UserState()
    data class IsLoading(var state: Boolean = false) : UserState()
    data class Success(var id: Int, var name: String, var email: String, var role: Int) : UserState()
    data class Failed(var message: String) : UserState()
    data class IsLoad(var data: MutableList<User>) : UserState()
    data class IsLoadUserById(var user: User) : UserState()

    object reset : UserState()

}