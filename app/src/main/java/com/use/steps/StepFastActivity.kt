package com.use.steps

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
import com.use.steps.adapters.FastAdapter
import com.use.steps.models.GejalaCFUser
import com.use.steps.utils.Constants
import com.use.steps.viewmodels.*
import kotlinx.android.synthetic.main.activity_step_fast.*

class StepFastActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val TAG = "StepFastActivity"
        var dataListCFUser = mutableListOf<GejalaCFUser>()

    }

    private lateinit var fastAdapter: FastAdapter
    private lateinit var laporanViewModel: LaporanViewModel
    private lateinit var gejalaViewModel: GejalaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_fast)

        fast_navigation.selectedItemId = R.id.fast
        fast_navigation.setOnNavigationItemSelectedListener(this)

        laporanViewModel = ViewModelProviders.of(this).get(LaporanViewModel::class.java)
        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)
        gejalaViewModel.getState().observer(this) {
            Log.e(DetailFastActivity.TAG, "gejalaViewModel.getState() $it")
            handleUIGejala(it)
        }

        val userId = Constants.getID(this)
        Log.e(TAG, "UserId $userId")

        laporanViewModel.fetchLaporansByUser(userId.toString())

        laporanViewModel.getState().observe(this, Observer {
            Log.e(TAG, "laporanViewModel.getState $it")
            handleUI(it)

        })

        initRecyclerView()

        if (Constants.getRole(this) == 2) {
            fast_layout_cari.visibility = View.VISIBLE
            fast_search.setOnClickListener {
                val id = fast_no.text.toString()
                if (id.isEmpty()) showMsg("Harap Mengisi Nomor Hp Pasien") else laporanViewModel.fetchLaporansByUser(id)
            }
        } else fast_layout_cari.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()
        gejalaViewModel.fetchGejalasCFUser()
    }

    private fun initRecyclerView() {

        fastAdapter = FastAdapter(this)
        fast_rv.apply {
            layoutManager = LinearLayoutManager(this@StepFastActivity)
            adapter = fastAdapter
        }


    }

    private fun handleUIGejala(it: GejalaState) {

        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }

            is GejalaState.IsLoad -> {
//                calculateCFCombine(it.data)
            }
            is GejalaState.IsLoadCFUser -> {
                dataListCFUser.clear()
                dataListCFUser.addAll(it.data)
            }
            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
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
            is LaporanState.IsLoad ->{
                if (it.data.isEmpty())
                    fast_empty.visibility = View.VISIBLE
                 else
                    fast_empty.visibility = View.GONE



                fastAdapter.setList(it.data)

            }
            is LaporanState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_fast.visibility = View.VISIBLE
        } else {
            spin_kit_fast.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                startActivity(Intent(applicationContext, StepHomeActivity::class.java)).also { finish() }
                overridePendingTransition(0, 0)
                true
            }
            R.id.research -> {
                startActivity(Intent(applicationContext, StepResearchActivity::class.java)).also { finish() }
                overridePendingTransition(0, 0)
                true
            }
            R.id.manage -> {
                startActivity(Intent(applicationContext, StepManageActivity::class.java)).also { finish() }
                overridePendingTransition(0, 0)
                true
            }
            R.id.fast -> {

                true
            }
            R.id.tutor -> {
                startActivity(Intent(applicationContext, TutorialActivity::class.java)).also { finish() }
                overridePendingTransition(0, 0)
                true
            }
            else -> false
        }
    }
}