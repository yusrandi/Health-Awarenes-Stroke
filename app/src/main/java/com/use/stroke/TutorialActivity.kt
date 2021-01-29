package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_step_fast.*
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        tutor_navigation.selectedItemId = R.id.tutor
        tutor_navigation.setOnNavigationItemSelectedListener(this)

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
                true
            }
            else -> false
        }
    }
}