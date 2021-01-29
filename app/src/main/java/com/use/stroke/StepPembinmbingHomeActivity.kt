package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.use.stroke.adapters.PembimbingHomeAdapter
import com.use.stroke.utils.Constants
import com.use.stroke.viewmodels.HistoryState
import com.use.stroke.viewmodels.HistoryViewModel
import kotlinx.android.synthetic.main.activity_step_home.*
import kotlinx.android.synthetic.main.activity_step_pembinmbing_home.*
import kotlinx.android.synthetic.main.activity_video.*

class StepPembinmbingHomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val TAG = "StepPembinmbingHomeActivity"
    }

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var pembimbingHomeAdapter: PembimbingHomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_pembinmbing_home)

        pembimbing_home_navigation.selectedItemId = R.id.home
        pembimbing_home_navigation.setOnNavigationItemSelectedListener(this)

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        historyViewModel.getState().observer(this) {
            Log.e(TAG, "historyViewModel.getState() $it")
            handleUI(it)
        }

        initRecyclerView()


    }

    private fun initRecyclerView() {
        pembimbingHomeAdapter = PembimbingHomeAdapter(this)
        pembimbing_home_rv.apply {
            adapter = pembimbingHomeAdapter
            layoutManager = LinearLayoutManager(this@StepPembinmbingHomeActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
        historyViewModel.fetchHistorysByUser(Constants.getID(this))
    }

    private fun handleUI(it: HistoryState) {
        when (it) {
            is HistoryState.IsLoading -> isLoading(it.state)
            is HistoryState.IsError -> showMsg(it.err)
            is HistoryState.IsLoad -> {
                pembimbingHomeAdapter.setList(it.data)

            }
            is HistoryState.IsSuccess -> {
                showMsg(it.msg)
            }

            else -> showMsg("undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_step_home_pembimbing.visibility = View.VISIBLE
        } else {
            spin_kit_step_home_pembimbing.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                true
            }

            R.id.fast -> {
                startActivity(Intent(applicationContext, StepPembimbingFastActivity::class.java)).also { finish() }
                overridePendingTransition(100, 100)
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