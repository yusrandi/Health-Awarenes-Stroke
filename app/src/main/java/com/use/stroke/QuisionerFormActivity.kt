package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.use.stroke.adapters.PembimbingQuisionerAdapter
import com.use.stroke.adapters.QuisionerFormAdapter
import com.use.stroke.utils.Constants
import com.use.stroke.viewmodels.QuestionerState
import com.use.stroke.viewmodels.QuestionerViewModel
import com.use.stroke.viewmodels.QuisionerResponseState
import com.use.stroke.viewmodels.QuisionerResponseViewModel
import kotlinx.android.synthetic.main.activity_quisioner_form.*
import kotlinx.android.synthetic.main.activity_step_pembimbing_quisioner.*
import java.text.SimpleDateFormat
import java.util.*

class QuisionerFormActivity : AppCompatActivity() {

    companion object{
        const val TAG = "QuisionerFormActivity"
    }
    private lateinit var questionerViewModel: QuestionerViewModel
    private lateinit var quisionerFormAdapter: QuisionerFormAdapter
    private lateinit var questionerResponseViewModel: QuisionerResponseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quisioner_form)

        questionerViewModel = ViewModelProviders.of(this).get(QuestionerViewModel::class.java)
        questionerResponseViewModel = ViewModelProviders.of(this).get(QuisionerResponseViewModel::class.java)

        questionerResponseViewModel.getState().observer(this){
            Log.e(TAG, "questionerResponseViewModel.getState() $it")
            handleUIQuisionerResponse(it)
        }
        questionerViewModel.getState().observer(this){
            Log.e(TAG, "questionerViewModel.getState() $it")
            handleUI(it)
        }

        initRecyclerView()

        quisioner_form_done.setOnClickListener {
            if(quisionerFormAdapter.dataList.size == quisionerFormAdapter.itemSelect.size){
//                showMsg("data ${quisionerFormAdapter.itemSelect}")
                val sb = StringBuffer()
                for (i in 0 until quisionerFormAdapter.itemSelect.size) {
                    if (i > 0) {
                        sb.append(",")
                    }
                    sb.append(quisionerFormAdapter.itemSelect[i])
                }
                questionerResponseViewModel.store(Constants.getID(this),sb.toString(),getCurrentTime())
            }else{
//                showMsg("tdak sama")
            }
        }
    }

    private fun handleUIQuisionerResponse(it: QuisionerResponseState) {
        when(it){
            is QuisionerResponseState.IsLoading -> {isLoading(it.state)}
            is QuisionerResponseState.IsSuccess -> {startActivity(Intent(this, StepPembimbingQuisionerActivity::class.java)).also { finish() }}
            is QuisionerResponseState.IsError -> {showMsg(it.err)}

            else -> showMsg("Undefined")
        }
    }

    override fun onStart() {
        super.onStart()
        questionerViewModel.fetchQuisioners()

    }
    private fun getCurrentTime(): String {
        return SimpleDateFormat("EEEE, dd MMMM HH:mm", Locale.getDefault()).format(Date())
    }
    private fun handleUI(it: QuestionerState) {
        when(it){
            is QuestionerState.IsLoading -> {isLoading(it.state)}
            is QuestionerState.IsSuccess -> {showMsg(it.msg)}
            is QuestionerState.IsError -> {showMsg(it.err)}
            is QuestionerState.IsLoad -> {
                quisionerFormAdapter.setList(it.data)
            }
        }
    }

    private fun initRecyclerView() {

        val dataListSelected = mutableListOf<Int>()

        val response = intent.getStringExtra("response")
        if(response != null){
            response.split(",").forEach {
                dataListSelected.add(it.toInt())
            }

            quisioner_form_done.visibility = View.GONE
            Log.e(TAG, "response $response, data $dataListSelected")

        }

        quisionerFormAdapter = QuisionerFormAdapter(this@QuisionerFormActivity)
        quisionerFormAdapter.setListSelected(dataListSelected)

        quisioner_form_rv.apply {
            adapter = quisionerFormAdapter
            layoutManager = LinearLayoutManager(this@QuisionerFormActivity)
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_quisioner_form.visibility = View.VISIBLE
        } else {
            spin_kit_quisioner_form.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

}
