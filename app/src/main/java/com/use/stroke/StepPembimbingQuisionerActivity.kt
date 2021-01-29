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
import com.use.stroke.adapters.PembimbingQuisionerAdapter
import com.use.stroke.viewmodels.*
import kotlinx.android.synthetic.main.activity_step_pembimbing_quisioner.*
import kotlinx.android.synthetic.main.activity_step_pembinmbing_home.*

class StepPembimbingQuisionerActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    companion object{
        const val TAG = "StepPembimbingQuisionerActivity"
    }

    private lateinit var questionerResponseViewModel: QuisionerResponseViewModel

    private lateinit var quisionerAdapter: PembimbingQuisionerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_pembimbing_quisioner)

        pembimbing_quisioner_navigation.selectedItemId = R.id.tutor
        pembimbing_quisioner_navigation.setOnNavigationItemSelectedListener(this)

        questionerResponseViewModel = ViewModelProviders.of(this).get(QuisionerResponseViewModel::class.java)



        questionerResponseViewModel.getState().observer(this){
            Log.e(TAG, "questionerResponseViewModel.getState() $it")
            handleUI(it)
        }
        initRecyclerView()

        pembimbing_quisioner_add.setOnClickListener { startActivity(Intent(this, QuisionerFormActivity::class.java)) }
    }

    override fun onStart() {
        super.onStart()
        questionerResponseViewModel.fetchQuisionersResponse()
    }

    private fun handleUI(it: QuisionerResponseState) {
        when(it){
            is QuisionerResponseState.IsLoading -> {isLoading(it.state)}
            is QuisionerResponseState.IsSuccess -> {showMsg(it.msg)}
            is QuisionerResponseState.IsError -> {showMsg(it.err)}
            is QuisionerResponseState.IsLoad -> {quisionerAdapter.setList(it.data)}
        }
    }

    private fun initRecyclerView() {
        quisionerAdapter = PembimbingQuisionerAdapter(this@StepPembimbingQuisionerActivity)
        pembimbing_quisioner_rv.apply {
            adapter = quisionerAdapter
            layoutManager = LinearLayoutManager(this@StepPembimbingQuisionerActivity)
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_step_quisioner_pembimbing.visibility = View.VISIBLE
        } else {
            spin_kit_step_quisioner_pembimbing.visibility = View.GONE
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
                startActivity(Intent(applicationContext, StepPembimbingFastActivity::class.java)).also { finish() }
                overridePendingTransition(100, 100)
                true
            }
            R.id.tutor -> {

                true
            }

            else -> false
        }

    }
}