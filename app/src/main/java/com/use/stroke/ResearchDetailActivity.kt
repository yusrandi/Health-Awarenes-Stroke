package com.use.stroke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.use.stroke.datas.ResearchData
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

    }
}