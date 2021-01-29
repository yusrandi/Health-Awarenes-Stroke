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
        val email = auth_et_email.text.toString().trim()
        val pass = auth_et_password.text.toString().trim()

        val emailPembimbing = auth_et_email_pembimbing.text.toString().trim()

        if(role!=3){
            if (name.isNotEmpty()) {
                if (validateField(email, pass)) {
                    viewModel.register(User(name = name, email = email, password = pass, role = role),User(name = name, email = email, password = pass, role = role),0)
                    showMsg("userSignUp")

                }
            } else auth_layout_username.error = "This field is required"
        }else{
            val namePembimbing = auth_et_user_pembimbing.text.toString().trim()
            val passPembimbing = auth_et_password_pembimbing.text.toString().trim()

            val user = User(name = name, email = email, password = pass, role = role)
            val userPembimbing = User(name = namePembimbing, email = emailPembimbing, password = passPembimbing, role = role)

            if(validateFieldPembimbing(namePembimbing, emailPembimbing, passPembimbing) && validateField(email, pass)){
                if (name.isNotEmpty()) {
                    if (validateField(email, pass)) {
                        viewModel.register(user, userPembimbing, doctorId)
                        showMsg("userSignUp")

                    }
                } else auth_layout_username.error = "This field is required"
            }else{
                showMsg("Silahkan Mengisi Semua Field")
            }
        }



    }

    private fun userSignIn() {

        val email = auth_et_email.text.toString().trim()
        val pass = auth_et_password.text.toString().trim()

        if (validateField(email, pass)) {
            showMsg("userSignIn")
            viewModel.login(email, pass)
        }
    }

    private fun regisState() {

        state = false
        auth_title.text = "Sign Up"
        auth_label.text = "Already Have an Account"
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
                role = index+1
                if(index == 2) auth_spinner_dokter.visibility = View.VISIBLE else auth_spinner_dokter.visibility = View.GONE

            }
        }
        auth_spinner_dokter.apply {
            setItems(dataListSpinnerDokter)
            setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                val data = allUserDokterData[index]
                showMsg("Index Terpilih $data")
                doctorId = data.id!!
                auth_layout_form_pembimbing.visibility = View.VISIBLE


            }
        }

    }

    private fun loginState() {
        state = true
        auth_title.text = "Sign In"
        auth_label.text = "Create Account"
        auth_btn_submit.text = "Sign In"
        auth_layout_username.visibility = View.GONE
        auth_layout_spinner.visibility = View.GONE

    }

    private fun validateField(email: String, pass: String): Boolean {
        reset()
        return if (email.isEmpty()) {
            auth_layout_email.error = "This field is required"
            false
        } else if (!Constants.isValidEmail(email)) {
            auth_layout_email.error = "Email not valid"
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

    private fun validateFieldPembimbing(name:String, email: String, pass: String): Boolean {
        reset()
        return if(name.isEmpty()){
            auth_layout_username_pembimbing.error = "This field is required"
            false
        }else if (email.isEmpty()) {
            auth_layout_email_pembimbing.error = "This field is required"
            false
        } else if (!Constants.isValidEmail(email)) {
            auth_layout_email_pembimbing.error = "Email not valid"
            false
        } else {
            if (pass.isEmpty()) {
                auth_layout_password_pembimbing.error = "This field is required"
                false
            } else {
                if (pass.length < 8) {
                    auth_layout_password_pembimbing.error = "password must min 8 length"
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
                Constants.setAuth(this, it.id, it.name, it.role)
                starApp(it.role)
                showMsg("Success")

            }
            is UserState.IsLoad -> {
                allUserData.addAll(it.data)
                it.data.forEach {
                    allUserDokterData.add(it)
                    Log.e(TAG, "Name ${it.name}, Role ${it.role}")
                    if(it.role == 1) dataListSpinnerDokter.add(it.name.toString())
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
    private fun starApp(role:Int) {

        if(role==2){
            startActivity(Intent(this, StepPembinmbingHomeActivity::class.java)).also { finish() }
        }else{
            startActivity(Intent(this, StepHomeActivity::class.java)).also { finish() }

        }
    }
}