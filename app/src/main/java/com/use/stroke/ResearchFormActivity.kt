package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.use.stroke.utils.MessageHandler
import com.use.stroke.viewmodels.ResearchState
import com.use.stroke.viewmodels.ResearchViewModel
import kotlinx.android.synthetic.main.activity_research_form.*
import kotlinx.android.synthetic.main.activity_step_research.*

class ResearchFormActivity : AppCompatActivity(), MessageHandler.MsgHandler {
    companion object {
        const val TAG = "ResearchFormActivity"
    }

    private lateinit var researchViewModel: ResearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research_form)

        researchViewModel = ViewModelProviders.of(this).get(ResearchViewModel::class.java)

        researchViewModel.getState().observer(this) {
            Log.e(TAG, "researchViewModel.getState $it")
            handleUI(it)
        }

        val status = intent.getStringExtra("status")
        val id = intent.getIntExtra("id", 0)
        val titleData = intent.getStringExtra("title")
        val descData = intent.getStringExtra("desc")

        research_form_title.setText(titleData)
        research_form_desc.setText(descData)

        research_form_add.text = status
        research_form_add.setOnClickListener {
            val title = research_form_title.text.trim().toString()
            val desc = research_form_desc.text.trim().toString()

            if (researchViewModel.validate(title, desc)) {
                if (status == "Create")
                    researchViewModel.store(title, desc)
                else
                    researchViewModel.update(id, title, desc)

            }
        }

    }

    private fun handleUI(it: ResearchState) {
        when (it) {
            is ResearchState.reset -> {
                setTitleError(null)
                setDescError(null)
            }
            is ResearchState.ShowToast -> showMsg(it.message.toString())
            is ResearchState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is ResearchState.Validate -> {
                it.title?.let {
                    setTitleError(it)
                }
                it.desc?.let {
                    setDescError(it)
                }
            }
            is ResearchState.IsSuccess -> {
                alertSuccess(it.msg)

            }
            is ResearchState.IsLoad -> {
            }
            is ResearchState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun setTitleError(err: String?) {
        research_form_title_layout.error = err
    }

    private fun setDescError(err: String?) {
        research_form_desc_layout.error = err
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_research_form.visibility = View.VISIBLE
        } else {
            spin_kit_research_form.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    override fun alertWarning(msg: String) {

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