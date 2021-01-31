package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.use.stroke.models.User
import com.use.stroke.utils.Constants
import com.use.stroke.viewmodels.UserState
import com.use.stroke.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_auth.*
import java.lang.Exception

class AuthActivity : AppCompatActivity() {
    private var state = true
    private var role = 1

    companion object {
        const val TAG = "AuthActivity"
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    private var dataListSpinner = mutableListOf<String>()
    private var dataListSpinnerDokter = mutableListOf<String>()
    private var allUserData = mutableListOf<User>()
    private var allUserDokterData = mutableListOf<User>()

    private var doctorId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (state) loginState() else regisState()

        auth_btn_submit.setOnClickListener {
            if (state) loginState() else regisState()
        }
        auth_label.setOnClickListener {
            if (state) regisState() else loginState()
        }

        auth_btn_submit.setOnClickListener {
            if (state) userSignIn() else userSignUp()
//            starApp()
        }
        viewModel.fetchAllUser()
        viewModel.getState().observe(this, Observer {
            Log.e(TAG, "getState $it")
            handleUIState(it)
        })


    }

    private fun userSignUp() {
        val name = auth_et_user.text.toString().trim()
        val phone = auth_et_email.text.toString().trim()
        val pass = auth_et_password.text.toString().trim()

        val emailPembimbing = auth_et_email_pembimbing.text.toString().trim()

        if (role != 3) {
            if (name.isNotEmpty()) {
                if (validateField(phone, pass)) {
                    viewModel.register(User(name = name, phone = phone, password = pass, role = role), 0)
//                    showMsg("userSignUp")

                }
            } else auth_layout_username.error = "This field is required"
        } else {

            val user = User(name = name, phone = phone, password = pass, role = role)

            if (name.isNotEmpty()) {
                if (validateField(phone, pass)) {
                    viewModel.register(user, doctorId)
//                        showMsg("userSignUp")

                }
            } else auth_layout_username.error = "This field is required"

        }


    }

    private fun userSignIn() {

        val email = auth_et_email.text.toString().trim()
        val pass = auth_et_password.text.toString().trim()

        if (validateField(email, pass)) {
//            showMsg("userSignIn")
            viewModel.login(email, pass)
        }
    }

    private fun regisState() {

        state = false
        auth_title.text = "Selamat Datang"
        auth_label.text = "Sudah punya akun"
        auth_btn_submit.text = "Sign up"
        auth_layout_username.visibility = View.VISIBLE
        auth_layout_spinner.visibility = View.VISIBLE

        dataListSpinner.clear()
        dataListSpinner.add("Dokter")
        dataListSpinner.add("Pendamping")
        dataListSpinner.add("Pasien")

        auth_spinner.apply {
            setItems(dataListSpinner)
            setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
//                showMsg("Index Terpilih $index")
                role = index + 1
                if (index == 2) auth_spinner_dokter.visibility = View.VISIBLE else auth_spinner_dokter.visibility =
                    View.GONE

            }
        }
        auth_spinner_dokter.apply {
            setItems(dataListSpinnerDokter)
            setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                val data = allUserDokterData[index]
//                showMsg("Index Terpilih $data")
                doctorId = data.id!!
//                auth_layout_form_pembimbing.visibility = View.VISIBLE


            }
        }

    }

    private fun loginState() {
        state = true
        auth_title.text = "Silahkan Masuk"
        auth_label.text = "Member baru ? Buat akun"
        auth_btn_submit.text = "Sign In"
        auth_layout_username.visibility = View.GONE
        auth_layout_spinner.visibility = View.GONE

    }

    private fun validateField(phone: String, pass: String): Boolean {
        reset()
        return if (phone.isEmpty()) {
            auth_layout_email.error = "This field is required"
            false
        } else if (phone.length < 10) {
            auth_layout_email.error = "phone must min 10 length"
            false
        } else {
            if (pass.isEmpty()) {
                auth_layout_password.error = "This field is required"
                false
            } else {
                if (pass.length < 8) {
                    auth_layout_password.error = "password must min 8 length"
                    false
                } else {
                    true
                }
            }
        }

        return false
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
                starApp(it.role)
                showMsg("Success")

            }
            is UserState.IsLoad -> {
                allUserData.addAll(it.data)
                it.data.forEach {
                    allUserDokterData.add(it)
                    Log.e(TAG, "Name ${it.name}, Role ${it.role}")
                    if (it.role == 1) dataListSpinnerDokter.add(it.name.toString())
                }
            }
            is UserState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_signin.visibility = View.VISIBLE
        } else {
            spin_kit_signin.visibility = View.GONE
        }
    }

    private fun reset() {
        auth_layout_email.error = null
        auth_layout_email_pembimbing.error = null
        auth_layout_password.error = null
        auth_layout_password_pembimbing.error = null
        auth_layout_username.error = null
        auth_layout_username_pembimbing.error = null

    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    private fun starApp(role: Int) {

        startActivity(Intent(this, StepHomeActivity::class.java)).also { finish() }

    }
}