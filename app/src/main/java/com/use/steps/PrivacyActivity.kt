package com.use.steps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala.doctor_manage_gejala_back
import kotlinx.android.synthetic.main.activity_privacy.privacy_back

class PrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        privacy_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}