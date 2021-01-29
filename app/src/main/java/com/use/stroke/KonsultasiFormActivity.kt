package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.powerspinner.*
import com.use.stroke.adapters.GejalaAdapter
import com.use.stroke.models.Gejala
import com.use.stroke.viewmodels.GejalaState
import com.use.stroke.viewmodels.GejalaViewModel
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_konsultasi_form.*
import kotlinx.android.synthetic.main.activity_step_fast.*

class KonsultasiFormActivity : AppCompatActivity() {

    companion object {
        var ITEMSELECT = ArrayList<Int>()
        var ITEMSELECTKEYAKINAN = ArrayList<Int>()
        const val TAG = "KonsultasiFormActivity"
        var dataList = mutableListOf<Gejala>()

    }

    private lateinit var gejalaAdapter: GejalaAdapter
    private lateinit var gejalaViewModel: GejalaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konsultasi_form)

        initRecyclerView()

        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)

        gejalaViewModel.fetchGejalas()

        gejalaViewModel.getState().observe(this, Observer {
            Log.e(TAG, "getState $it")
            handleUI(it)
        })
        gejalaViewModel.getGejala().observe(this, Observer {

            dataList.clear()
            dataList.addAll(it)
            gejalaAdapter.setList(it as MutableList<Gejala>)

        })

        konsul_done.setOnClickListener {
            if (gejalaAdapter.itemSelect.size != dataList.size && gejalaAdapter.itemSelectKeyakinan.size != dataList.size ) showMsg("Silahkan Mengisi Semua Pilihan") else {

                ITEMSELECT = gejalaAdapter.itemSelect
                ITEMSELECTKEYAKINAN = gejalaAdapter.itemSelectKeyakinan

                startActivity(Intent(this, ResultActivity::class.java)).also { finish() }

            }
        }
    }

    override fun onStart() {
        super.onStart()


    }

    private fun handleUI(it: GejalaState) {

        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is GejalaState.IsSuccess -> {

            }
            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun initRecyclerView() {

        gejalaAdapter = GejalaAdapter(this, mutableListOf())
        konsul_rv.apply {
            layoutManager = LinearLayoutManager(this@KonsultasiFormActivity)
            adapter = gejalaAdapter
        }

    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_konsultasi.visibility = View.VISIBLE
        } else {
            spin_kit_konsultasi.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

}