package com.use.stroke

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.use.stroke.utils.Constants
import com.use.stroke.viewmodels.GejalaState
import com.use.stroke.viewmodels.GejalaViewModel
import devlight.io.library.ArcProgressStackView
import kotlinx.android.synthetic.main.activity_konsul_first_step.*
import kotlinx.android.synthetic.main.activity_step_manage.*
import kotlinx.android.synthetic.main.activity_step_research.*
import java.util.ArrayList

class StepManageActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object{
        const val TAG = "StepManageActivity"
    }
    private lateinit var gejalaViewModel: GejalaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_manage)

        manage_navigation.selectedItemId = R.id.manage
        manage_navigation.setOnNavigationItemSelectedListener(this)

        initProgressArch()

        step_manage_patient.setOnClickListener { startActivity(Intent(this, KonsulFirstStepActivity::class.java)) }
        step_manage_video.setOnClickListener { startActivity(Intent(this, ViedeoMenuActivity::class.java)) }
        step_manage_doctor.setOnClickListener { startActivity(Intent(this, DoctorManageGejalaActivity::class.java)) }

        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)
        gejalaViewModel.fetchGejalas()
        gejalaViewModel.getState().observer(this){
            handleUi(it)
        }
        checkLogInRoleUser(Constants.getRole(this))

    }

    private fun handleUi(it: GejalaState) {
        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is GejalaState.IsLoad -> {
                step_manage_persen.text = "${it.data.size}"
                step_manage_persen_label.text = "Pertanyaan"
            }

            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }
    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_step_manage.visibility = View.VISIBLE
        } else {
            spin_kit_step_manage.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


    private fun checkLogInRoleUser(role: Int) {
        when(role){
            1 -> {
                step_manage_patient.visibility = View.GONE
                step_manage_persen.text = ". . ."
                step_manage_persen_label.text = ". . ."
            }
            2 -> {
                step_manage_doctor.visibility = View.GONE
                step_manage_patient.visibility = View.GONE

                step_manage_pendamping_form.visibility = View.VISIBLE
                step_manage_pendamping_form.setOnClickListener {
                    startActivity(Intent(this, StepPembimbingQuisionerActivity::class.java))
                }

            }
            3 -> {
                step_manage_doctor.visibility = View.GONE
            }
        }
    }



    private fun initProgressArch() {

        val modelCount = 4

        // Parsed colors
        val mStartColors = IntArray(modelCount)
        val mEndColors = IntArray(modelCount)


        val mArcProgressStackView: ArcProgressStackView = findViewById(R.id.step_manage_apsv)
        mArcProgressStackView.shadowColor = R.color.colorPrimary
        mArcProgressStackView.setIsShadowed(true)
        mArcProgressStackView.shadowDistance = 2f

        // Get colors
        val startColors = resources.getStringArray(R.array.devlight)
        val endColors = resources.getStringArray(R.array.default_preview)
        val bgColors = resources.getStringArray(R.array.medical_express)

        // Parse colors
        for (i in 0 until modelCount) {
            mStartColors[i] = Color.parseColor(startColors[i])
            mEndColors[i] = Color.parseColor(endColors[i])
        }


        val models = ArrayList<ArcProgressStackView.Model>()
        models.add(ArcProgressStackView.Model("hahahha", 70f, R.color.colorPrimaryDark, mStartColors[0]))
        models.add(ArcProgressStackView.Model("ha", 70f, R.color.colorPrimaryDark, mStartColors[1]))
        models.add(ArcProgressStackView.Model("haha", 70f, R.color.colorPrimaryDark, mStartColors[2]))
        models.add(ArcProgressStackView.Model("hahaha", 70f, R.color.colorPrimaryDark, mStartColors[3]))
        mArcProgressStackView.models = models
    }

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
}