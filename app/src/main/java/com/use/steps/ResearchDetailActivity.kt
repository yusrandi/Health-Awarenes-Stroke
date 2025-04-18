package com.use.steps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala_form.konsul_first_step_back
import kotlinx.android.synthetic.main.activity_research_detail.*

class ResearchDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research_detail)

        StepResearchActivity.dataList.forEach {
            if(it.id == intent.getIntExtra("id",0)){
                research_detail_title.text = it.title
                research_detail_desc.text = it.desc.replace("//", "")
            }
        }
        btn_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}