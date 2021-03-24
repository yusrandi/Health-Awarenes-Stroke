package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.*
import com.use.stroke.utils.Constants
import com.use.stroke.utils.MessageHandler
import com.use.stroke.viewmodels.HistoryState
import com.use.stroke.viewmodels.HistoryViewModel
import com.use.stroke.viewmodels.ResearchViewModel
import kotlinx.android.synthetic.main.activity_research_form.*
import kotlinx.android.synthetic.main.activity_video.*
import java.text.SimpleDateFormat
import java.util.*


class VideoActivity : AppCompatActivity(), MessageHandler.MsgHandler {
    companion object {
        const val TAG = "VideoActivity"
        var STREAM_URL = "/1.mp4"
        private var dataStreamUrl = mutableListOf("/1.mp4","/2.mp4","/3.mp4","/4.mp4")

    }

    private lateinit var player: SimpleExoPlayer
    private var watchFinished = false

    private lateinit var historyViewModel: HistoryViewModel
    private var indexVideo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        initPlayer()

        Log.e(TAG, "CurrentTime ${getCurrentTime()}")
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        historyViewModel.getState().observer(this) {
            Log.e(TAG, "historyViewModel.getState() $it")
            handleUI(it)
        }




    }

    private fun handleUI(it: HistoryState) {
        when (it) {
            is HistoryState.IsLoading -> isLoading(it.state)
            is HistoryState.IsError -> showMsg(it.err)
            is HistoryState.IsSuccess -> {
                starApp()
            }

            else -> showMsg("undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_video.visibility = View.VISIBLE
        } else {
            spin_kit_video.visibility = View.GONE
        }
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(this).build()

        video_view.player = player

        val index = intent.getIntExtra("index", 0)
        Log.e(TAG, "Video URL ${dataStreamUrl[index]}, index $index")
        indexVideo = index+1
        val mediaItem: MediaItem = MediaItem.fromUri("${Constants.BASE_API}${dataStreamUrl[index]}")

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        player.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
//                showMsg("onPlayerError $error")
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                showMsg("onPlayerStateChanged $playbackState")
                when (playbackState) {

                    STATE_BUFFERING -> {
                        spin_kit_video.visibility = View.VISIBLE
                        Log.d(TAG, "onPlayerStateChanged - STATE_BUFFERING")
//                        showMsg("onPlayerStateChanged - STATE_BUFFERING")
                    }
                    STATE_READY -> {
                        spin_kit_video.visibility = View.INVISIBLE
                        Log.d(TAG, "onPlayerStateChanged - STATE_READY")
//                        showMsg("onPlayerStateChanged - STATE_READY")

                    }
                    STATE_IDLE -> {
                        Log.d(TAG, "onPlayerStateChanged - STATE_IDLE")
//                        showMsg("onPlayerStateChanged - STATE_IDLE")
                    }
                    STATE_ENDED -> {
                        Log.d(TAG, "onPlayerStateChanged - STATE_ENDED")
//                        showMsg("onPlayerStateChanged - STATE_ENDED")
                        watchFinished = true

                    }

                }
            }
        })
    }


    override fun onBackPressed() {
        val event = "Telah Menonton Video $indexVideo"
       if(Constants.getRole(this) == 3){
           if (watchFinished) historyViewModel.store(
               Constants.getID(this),
               event,
               getCurrentTime()
           ) else alertWarning("")
       }else{
           player.release()
           super.onBackPressed()
       }

    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("EEEE, dd MMMM HH:mm", Locale.getDefault()).format(Date())
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun alertWarning(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Maaf Belum Selesai") //
            .setContentText("lanjutkan Menonton ?")
            .setConfirmText("Ya")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .setCancelButton("Tidak") { ssDialog ->
                ssDialog.dismissWithAnimation()

                starApp()
            }
            .show()
    }

    override fun alertSuccess(msg: String) {

    }

    private fun starApp() {
        player.release()
        startActivity(Intent(this, StepManageActivity::class.java)).also { finish() }
    }

}
