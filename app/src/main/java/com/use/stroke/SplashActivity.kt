package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.use.stroke.utils.Constants
import java.util.*

class SplashActivity : AppCompatActivity() {
    private var p: Int = 0
    private var progressBar: ProgressBar? = null
    private var timer: Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        progressBar = findViewById<View>(R.id.progress) as ProgressBar
        progressBar!!.progress = p
        progressBar!!.max = 99

        val period: Long = 100
        timer.schedule(object : TimerTask() {
            override fun run() {
                //this repeats every 100 ms
                if (p < 100) {
                    runOnUiThread {

                    }
                    progressBar!!.progress = p
                    p++
                } else {
                    //closing the timer
                    timer.cancel()
                    starApp()
                }
            }
        }, 0, period)
    }

    private fun starApp() {
        if (Constants.getAuth(this)) {

            startActivity(Intent(this, StepHomeActivity::class.java)).also { finish() }

        } else {
            startActivity(Intent(this, AuthActivity::class.java)).also { finish() }

        }

    }
}