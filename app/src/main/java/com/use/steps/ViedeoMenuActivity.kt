package com.use.steps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala_form.konsul_first_step_back
import kotlinx.android.synthetic.main.activity_viedeo_menu.*

class ViedeoMenuActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viedeo_menu)

        menu_video_0.setOnClickListener(this)
        menu_video_1.setOnClickListener(this)
        menu_video_2.setOnClickListener(this)
        menu_video_3.setOnClickListener(this)
        menu_video_4.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var value = 0
        when(v!!.id){
            R.id.menu_video_0 ->{
                value = 0
            }
            R.id.menu_video_1 ->{
                value = 1
            }
            R.id.menu_video_2 ->{
                value = 2
            }
            R.id.menu_video_3 ->{
                value = 3
            }
            R.id.menu_video_4 ->{
                value = 4
            }
        }

        startActivity(Intent(this, VideoActivity::class.java).apply { putExtra("index", value) })
        btn_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}