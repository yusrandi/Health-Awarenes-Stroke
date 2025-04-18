package com.use.steps

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.use.steps.utils.Constants
import com.use.steps.viewmodels.*
import devlight.io.library.ArcProgressStackView
import kotlinx.android.synthetic.main.activity_step_home.*
import java.util.ArrayList

class StepHomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val TAG = "StepHomeActivity"
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            RelasiViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_home)

        home_navigation.selectedItemId = R.id.home
        home_navigation.setOnNavigationItemSelectedListener(this)

        viewModel.getDoctorCount(Constants.getID(this))
        viewModel.getState().observer(this) {
            handleUIState(it)
        }

        initProgressArch()

        Log.e(TAG, "ID ${Constants.getID(this)}, Role ${Constants.getRole(this)}")

        checkLogInRoleUser(Constants.getRole(this))
        step_home_name.text = Constants.getName(this)
        step_home_nomor.text = Constants.getPhoneNumber(this)



        step_home_go_profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun handleUIState(it: RelasiState) {
        when (it) {

            is RelasiState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }

            is RelasiState.IsSuccess -> {
                if(Constants.getRole(this) == 1){
                    step_home_persen.text = it.msg
                    step_home_persen_label.text = "Jumlah Pasien"
                }
            }

            is RelasiState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_step_home.visibility = View.VISIBLE
        } else {
            spin_kit_step_home.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    private fun checkLogInRoleUser(role: Int) {
        when (role) {
            1 -> {
                step_home_persen.text = "..."
                step_home_persen_label.text = "..."

                step_home_go_test_label.text = "Kelola Gejala"
                step_home_go_test.setOnClickListener {
                    startActivity(Intent(this, DoctorManageGejalaActivity::class.java))
                }
            }
            2 -> {
                step_home_persen.text = "..."
                step_home_persen_label.text = "..."

                step_home_go_test_label.text = "History Aktivitas Pasien"
                step_home_go_test.setOnClickListener {
                    startActivity(Intent(this, StepPembinmbingHomeActivity::class.java))
                }
            }
            3 -> {
                step_home_persen.text = "${Constants.getResult(this)}%"
                step_home_persen_label.text = "Kemungkinan Mengidap Penyakit Stroke"
                step_home_go_test.visibility = View.VISIBLE

                step_home_go_test_label.text = "Mulai Konsultasi"
                step_home_go_test.setOnClickListener {
                    startActivity(Intent(this, KonsulFirstStepActivity::class.java))
                }
            }
            4 -> {
                step_home_persen.text = "..."
                step_home_persen_label.text = "..."

                step_home_go_test_label.text = "History Aktivitas Pasien"
                step_home_go_test.setOnClickListener {
                    startActivity(Intent(this, StepPembinmbingHomeActivity::class.java))
                }
            }
        }
    }

    private fun initProgressArch() {
        val modelCount = 4

        // Parsed colors
        val mStartColors = IntArray(modelCount)
        val mEndColors = IntArray(modelCount)


        var mArcProgressStackView: ArcProgressStackView = findViewById(R.id.apsv)
        mArcProgressStackView.shadowColor = R.color.colorPrimary
        mArcProgressStackView.setIsShadowed(true)
        mArcProgressStackView.shadowDistance = 20f

        // Get colors
        val startColors = resources.getStringArray(R.array.default_preview)
        val endColors = resources.getStringArray(R.array.medical_express)
        val bgColors = resources.getStringArray(R.array.medical_express)

        // Parse colors
        for (i in 0 until modelCount) {
            mStartColors[i] = Color.parseColor(startColors[i])
            mEndColors[i] = Color.parseColor(endColors[i])
        }

        val models = ArrayList<ArcProgressStackView.Model>()
//        models.add(ArcProgressStackView.Model("Penghapusan", 65f, R.color.colorPrimary, mStartColors[3]))
//        models.add(ArcProgressStackView.Model("Pemanfaatan", 60f, R.color.colorPrimary, mStartColors[2]))
//        models.add(ArcProgressStackView.Model("Pengadaan", 55f, R.color.colorPrimary, mStartColors[1]))
        models.add(
            ArcProgressStackView.Model(
                "",
                Constants.getResult(this).toFloat(),
                R.color.colorPrimaryDark,
                R.color.colorAccent
            )
        )
//        models.add(ArcProgressStackView.Model("Perencanaan", 50f, R.color.colorPrimary, mStartColors[0]))
        mArcProgressStackView.models = models
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
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