package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.use.stroke.adapters.FastAdapter
import com.use.stroke.models.Laporan
import com.use.stroke.utils.Constants
import com.use.stroke.viewmodels.LaporanState
import com.use.stroke.viewmodels.LaporanViewModel
import kotlinx.android.synthetic.main.activity_step_fast.*
import kotlinx.android.synthetic.main.activity_step_pembimbing_fast.*
import kotlinx.android.synthetic.main.activity_step_pembinmbing_home.*

class StepPembimbingFastActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val TAG = "StepPembimbingFastActivity"
    }

    private lateinit var fastAdapter: FastAdapter
    private lateinit var laporanViewModel: LaporanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_pembimbing_fast)

        pembimbing_fast_navigation.selectedItemId = R.id.fast
        pembimbing_fast_navigation.setOnNavigationItemSelectedListener(this)

        laporanViewModel = ViewModelProviders.of(this).get(LaporanViewModel::class.java)



        laporanViewModel.getState().observe(this, Observer {
            Log.e(TAG, "laporanViewModel.getState $it")
            handleUI(it)

        })
        laporanViewModel.getLaporan().observe(this, Observer {
            if (it.isEmpty()) {
                pembimbing_fast_empty.visibility = View.VISIBLE
            }else{
                pembimbing_fast_empty.visibility = View.GONE

                fastAdapter.setList(it as MutableList<Laporan>)
            }
        })
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        val userId = Constants.getID(this)
        Log.e(TAG, "UserId $userId")
        laporanViewModel.fetchLaporansByUser(userId)

    }

    private fun initRecyclerView() {

        fastAdapter = FastAdapter(this)
        pembimbing_fast_rv.apply {
            layoutManager = LinearLayoutManager(this@StepPembimbingFastActivity)
            adapter = fastAdapter
        }


    }

    private fun handleUI(it: LaporanState) {

        when (it) {

            is LaporanState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is LaporanState.IsSuccess -> {

            }
            is LaporanState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_step_fast_pembimbing.visibility = View.VISIBLE
        } else {
            spin_kit_step_fast_pembimbing.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.home -> {
                startActivity(Intent(applicationContext, StepPembinmbingHomeActivity::class.java)).also { finish() }
                overridePendingTransition(100, 100)
                true
            }

            R.id.fast -> {

                true
            }
            R.id.tutor -> {
                startActivity(Intent(applicationContext, StepPembimbingQuisionerActivity::class.java)).also { finish() }
                overridePendingTransition(100, 100)
                true
            }

            else -> false
        }
    }
}