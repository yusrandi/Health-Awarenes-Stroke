package com.use.steps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.use.steps.adapters.VariabelAdapter
import com.use.steps.viewmodels.GejalaState
import com.use.steps.viewmodels.GejalaViewModel
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala.doctor_manage_gejala_back
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala_form.*

class DoctorManageGejalaFormActivity : AppCompatActivity() {
    companion object {
        const val TAG = "DoctorManageGejalaFormActivity"
    }

    private lateinit var variabelAdapter: VariabelAdapter
    private lateinit var gejalaViewModel: GejalaViewModel

    private var count = 0
    private var valueIdKategori = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_manage_gejala_form)

        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)

        gejalaViewModel.getState().observe(this, Observer {
            Log.e(TAG, "getState $it")
            handleUI(it)
        })

        form_gejala_jumlah.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                val ss = s.toString()
                if (ss == null || ss == "") count = 0 else count = ss.toInt()
                variabelAdapter = VariabelAdapter(this@DoctorManageGejalaFormActivity, count)
                form_gejala_rv.apply {
                    layoutManager = LinearLayoutManager(this@DoctorManageGejalaFormActivity)
                    adapter = variabelAdapter
                }


            }

        })
        form_gejala_kategori.apply {
            setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                valueIdKategori = index + 1
            }
        }

        form_gejala_add.setOnClickListener {
            val valueName = form_gejala_name.text

            if (valueIdKategori != 0) {
                if (valueName.isNotEmpty()) {
                    Log.e(TAG, "count $count, Size ${variabelAdapter.itemSelectVariabel.size}")
                    if (count == variabelAdapter.itemSelectVariabel.size) {
                        val sbVariabel = StringBuffer()
                        val sbCfs = StringBuffer()

                        for (i in 0 until variabelAdapter.itemSelectVariabel.size) {
                            if (i > 0) {
                                sbVariabel.append(",")
                            }
                            sbVariabel.append(variabelAdapter.itemSelectVariabel[i])
                        }
                        for (i in 0 until variabelAdapter.itemSelectCf.size) {
                            if (i > 0) {
                                sbCfs.append(",")
                            }
                            sbCfs.append(variabelAdapter.itemSelectCf[i])
                        }

                        Log.e(TAG, "Name ${valueName.toString()}, variabel ${sbVariabel.toString()}, cfs ${sbCfs.toString()}, kat $valueIdKategori")
                        gejalaViewModel.cfPakarStore(
                            valueName.toString(),
                            sbVariabel.toString(),
                            sbCfs.toString(),
                            valueIdKategori
                        )

                    } else {
                        showMsg("Silahkan Mengisi form variabel terlebih dahulu")
                    }
                } else {
                    showMsg("Silahkan Mengisi Gejala terlebih dahulu")
                }
            } else {
                showMsg("Silahkan Memilih Kategori terlebih dahulu")
            }

            Log.e(TAG, "Variabels ${variabelAdapter.itemSelectVariabel}, Cfs ${variabelAdapter.itemSelectCf}")
        }

        konsul_first_step_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }

    private fun handleUI(it: GejalaState) {
        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is GejalaState.IsSuccess -> {
                startActivity(Intent(this, DoctorManageGejalaActivity::class.java)).also { finish() }
            }
            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_form_gejala.visibility = View.VISIBLE
        } else {
            spin_kit_form_gejala.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


}