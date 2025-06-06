package com.use.steps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.use.steps.adapters.PembimbingHomeAdapter
import com.use.steps.viewmodels.HistoryState
import com.use.steps.viewmodels.HistoryViewModel
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala_form.konsul_first_step_back
import kotlinx.android.synthetic.main.activity_step_pembinmbing_home.*

class StepPembinmbingHomeActivity : AppCompatActivity(){
    companion object {
        const val TAG = "StepPembinmbingHomeActivity"
    }

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var pembimbingHomeAdapter: PembimbingHomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_pembinmbing_home)

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        historyViewModel.getState().observer(this) {
            Log.e(TAG, "historyViewModel.getState() $it")
            handleUI(it)
        }

        initRecyclerView()

        pembimbing_home_search.setOnClickListener {
            val id = pembimbing_home_no.text.toString()
            if(id.isEmpty()) showMsg("Harap Mengisi Nomor Hp Pasien") else historyViewModel.fetchHistorysByUser(id)
        }
        btn_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

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

    }

    private fun handleUI(it: HistoryState) {
        when (it) {
            is HistoryState.IsLoading -> isLoading(it.state)
            is HistoryState.IsError -> showMsg(it.err)
            is HistoryState.IsLoad -> {
                if (it.data.isEmpty())
                    pembimbing_home_empty.visibility = View.VISIBLE
                else
                    pembimbing_home_empty.visibility = View.GONE

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


}