package com.use.stroke

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.use.stroke.models.User
import com.use.stroke.utils.Constants
import com.use.stroke.viewmodels.UserState
import com.use.stroke.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_profile.*
import android.R.attr.data
import android.content.Intent
import cn.pedant.SweetAlert.SweetAlertDialog
import com.himanshurawat.hasher.HashType
import com.himanshurawat.hasher.Hasher
import com.use.stroke.utils.MessageHandler


class ProfileActivity : AppCompatActivity(), MessageHandler.MsgHandler {
    companion object {
        const val TAG = "ProfileActivity"
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    private lateinit var userValue: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        viewModel.getState().observer(this) {
            it?.let {
                handleUIState(it)
            }
        }

        profile_back.setOnClickListener { starApp() }
        profile_update.setOnClickListener { updateData() }
        step_profile_go_out.setOnClickListener {
            Constants.clearToken(this)
            startActivity(Intent(this, SplashActivity::class.java)).also { finishAffinity() }
        }
    }

    private fun updateData() {
        val name = profile_name.text.toString().trim()
        val password = profile_name.text.toString().trim()
        val phone = profile_phone.text.toString().trim()

        userValue = User(id = Constants.getID(this), name = name, phone = phone, 0, password = password )

        if(name.isNotEmpty()){
            alertWarning("Ingin Mengubah Data Diri ?")
        }else{
            showMsg("Maaf Nama tdk boleh kosong")
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchUserById(Constants.getID(this).toString())
    }

    private fun handleUIState(it: UserState) {
        when (it) {

            is UserState.Error -> {
                isLoading(false)
                showMsg(it.err!!)
            }
            is UserState.Failed -> {
                isLoading(false)
                showMsg(it.message)
            }
            is UserState.Success -> {
                Constants.setAuth(this, it.id, it.name, it.role, it.email)
                profile_title_name.text = it.name
                showMsg("Success")
            }
            is UserState.IsLoadUserById -> {
                showUser(it.user)
            }
            is UserState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun showUser(user: User) {
        showMsg(user.name.toString())
        profile_title_name.text = user.name
        profile_name.setText(user.name)
        profile_phone.setText(user.phone)

    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_profile.visibility = View.VISIBLE
        } else {
            spin_kit_profile.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    override fun alertWarning(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Update Identitas") //
            .setContentText(msg)
            .setConfirmText("Ya, yakin")
            .setConfirmClickListener { sDialog ->

                viewModel.update(User(userValue.id, userValue.name, userValue.phone, 0, userValue.password))

                sDialog.dismissWithAnimation()
            }
            .setCancelButton("Batal") { ssDialog -> ssDialog.dismissWithAnimation() }
            .show()
    }

    override fun alertSuccess(msg: String) {

    }
    private fun starApp() {

        startActivity(Intent(this, StepHomeActivity::class.java)).also { finish() }

    }

}