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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.use.stroke.adapters.FastAdapter
import com.use.stroke.adapters.ResearchAdapter
import com.use.stroke.datas.ResearchData
import com.use.stroke.models.Research
import com.use.stroke.utils.Constants
import com.use.stroke.utils.MessageHandler
import com.use.stroke.viewmodels.LaporanState
import com.use.stroke.viewmodels.LaporanViewModel
import com.use.stroke.viewmodels.ResearchState
import com.use.stroke.viewmodels.ResearchViewModel
import kotlinx.android.synthetic.main.activity_step_fast.*
import kotlinx.android.synthetic.main.activity_step_home.*
import kotlinx.android.synthetic.main.activity_step_research.*

class StepResearchActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    MessageHandler.MsgHandler {

    companion object {
        const val TAG = "StepResearchActivity"
        var dataList = mutableListOf<Research>()

    }

    private lateinit var researchAdapter: ResearchAdapter
    private lateinit var researchViewModel: ResearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_research)

        research_navigation.selectedItemId = R.id.research
        research_navigation.setOnNavigationItemSelectedListener(this)

        researchViewModel = ViewModelProviders.of(this).get(ResearchViewModel::class.java)


        researchViewModel.fetchResearchs()
        researchViewModel.getState().observer(this) {
            Log.e(TAG, "researchViewModel.getState $it")
            handleUI(it)
        }

        initRecyclerView()


        if(Constants.getRole(this) == 1){
            step_research_add.visibility = View.VISIBLE
            step_research_add.setOnClickListener {
                startActivity(Intent(this, ResearchFormActivity::class.java).apply {
                    putExtra("status", "Create")
                })
            }
        }else{
            step_research_add.visibility = View.GONE

        }




    }

    private fun handleUI(it: ResearchState) {
        when (it) {

            is ResearchState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is ResearchState.IsSuccess -> {
                if (it.code == 2) {
                    alertSuccess(it.msg)
                }
            }
            is ResearchState.IsLoad -> {
                dataList.clear()
                dataList.addAll(it.data)
                researchAdapter.setList(it.data)
            }
            is ResearchState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_research.visibility = View.VISIBLE
        } else {
            spin_kit_research.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun initRecyclerView() {

        researchAdapter = ResearchAdapter(this)
        research_rv.apply {
            layoutManager = LinearLayoutManager(this@StepResearchActivity)
            adapter = researchAdapter
        }
        researchAdapter.setOnItemDeleteClickCallback(object : ResearchAdapter.OnItemDeleteClickCallback {
            override fun onItemClick(data: Research) {
                alertWarning(data.id.toString())
            }

        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                startActivity(Intent(applicationContext, StepHomeActivity::class.java)).also { finish() }
                overridePendingTransition(0, 0)
                true
            }
            R.id.research -> {

                true
            }
            R.id.manage -> {
                startActivity(Intent(applicationContext, StepManageActivity::class.java)).also { finish() }
                overridePendingTransition(0, 0)
                true
            }
            R.id.fast -> {
                startActivity(Intent(applicationContext, StepFastActivity::class.java)).also { finish() }
                overridePendingTransition(0, 0)
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

    override fun alertWarning(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Konfirmasi Penghapusan") //
            .setContentText("apakah anda yakin menghapus artikel ini ?")
            .setConfirmText("Ya, yakin")
            .setConfirmClickListener { sDialog ->

//                showMsg("telah dihapus $msg")
                researchViewModel.delete(msg.toInt())
                sDialog.dismissWithAnimation()
            }
            .setCancelButton("Batal") { ssDialog -> ssDialog.dismissWithAnimation() }
            .show()
    }

    override fun alertSuccess(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Berhasil") //
            .setContentText(msg)
            .setConfirmClickListener { sDialog ->

                startActivity(Intent(this, StepResearchActivity::class.java)).also { finish() }
                sDialog.dismissWithAnimation()
            }
            .show()
    }
}