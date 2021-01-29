package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.use.stroke.adapters.DoctorManageGejalaAdapter
import com.use.stroke.models.Gejala
import com.use.stroke.utils.MessageHandler
import com.use.stroke.viewmodels.*
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala.*
import kotlinx.android.synthetic.main.activity_konsultasi_form.*

class DoctorManageGejalaActivity : AppCompatActivity() , MessageHandler.MsgHandler{

    companion object{
        const val TAG = "DoctorManageGejalaActivity"
    }
    private lateinit var doctorManageGejalaAdapter: DoctorManageGejalaAdapter
    private lateinit var gejalaViewModel: GejalaViewModel
    private lateinit var cfPakarViewModel: CFPakarViewModel


    private var katId = 0
//    private var dataTitle = mutableListOf("First Step", "Second Step", "Third Step")
    private var dataSubTitle = mutableListOf("A. Faktor Risiko (KESEHATAN)", "B. Faktor Risiko (GAYA HIDUP)", "C. Gejala dan Ciri-Ciri Penderita Stroke?")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_manage_gejala)

        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)
        cfPakarViewModel = ViewModelProviders.of(this).get(CFPakarViewModel::class.java)

        initRecyclerView()

        gejalaViewModel.fetchGejalasByKategori(katId+1)
        gejalaViewModel.fetchGejalasCFUser()

        doctor_manage_title.text = "Form Kelola Gejala"
        doctor_manage_subtitle.text = dataSubTitle[katId]

        gejalaViewModel.getState().observe(this, Observer {
            Log.e(TAG, "getState $it")
            handleUI(it)
        })
        cfPakarViewModel.getState().observer(this){
            Log.e(TAG, "cfPakarViewModel.getState $it")
            handleUICFPakar(it)
        }

        doctor_manage_gejala_fab.setOnClickListener { startActivity(Intent(this, DoctorManageGejalaFormActivity::class.java)) }
        doctor_manage_next.setOnClickListener {

                Log.e(TAG, "Item Select ${doctorManageGejalaAdapter.itemSelect}")

                Log.e(TAG, "Kat ID ${katId+1}, data Size ${dataSubTitle.size}")
                if(katId+1 < dataSubTitle.size){

                    katId++
                    gejalaViewModel.fetchGejalasByKategori(katId+1)
                    doctor_manage_subtitle.text = dataSubTitle[katId]
                    initRecyclerView()

                }else{
                    doctor_manage_next.visibility = View.GONE

                }


        }
    }

    private fun handleUICFPakar(it: CFPakarState) {
        when (it) {

            is CFPakarState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is CFPakarState.IsSuccess -> {
                startActivity(Intent(this, DoctorManageGejalaActivity::class.java)).apply { finish() }
            }
            is CFPakarState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }


    private fun initRecyclerView() {

        doctorManageGejalaAdapter = DoctorManageGejalaAdapter(this)
        doctor_manage_gejala_rv.apply {
            layoutManager = LinearLayoutManager(this@DoctorManageGejalaActivity)
            adapter = doctorManageGejalaAdapter
        }
        doctorManageGejalaAdapter.setOnItemUpdateClickCallback(object : DoctorManageGejalaAdapter.OnItemUpdateClickCallback{
            override fun onItemClick(id: Int, cf: Double) {
                if(id != 0){
                    if(cf != 0.0){
                        alertWarning("1,$id,$cf")
                    }else{
                        showMsg("Harap Memilih Kategori CF Terlebih dahulu")

                    }
                }else{
                    showMsg("Harap Memilih Variabel Terlebih dahulu")
                }
            }



        })
        doctorManageGejalaAdapter.setOnItemDeleteClickCallback(object : DoctorManageGejalaAdapter.OnItemDeleteClickCallback{
            override fun onItemDeleteClick(id: Int) {
                if(id != 0){
                    alertWarning("2,$id")
                }else{
                    showMsg("Harap Memilih Variabel Terlebih dahulu")
                }

            }

        })
    }

    private fun handleUI(it: GejalaState) {

        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is GejalaState.IsLoad -> {
                doctor_manage_next.visibility = View.VISIBLE
                doctorManageGejalaAdapter.setList(it.data)

            }
            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_doctor_manage_gejala.visibility = View.VISIBLE
        } else {
            spin_kit_doctor_manage_gejala.visibility = View.GONE
        }
    }
    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    override fun alertWarning(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Konfirmasi") //
            .setContentText("apakah anda yakin ?")
            .setConfirmText("Ya, yakin")
            .setConfirmClickListener { sDialog ->

                val data = msg.split(",")
                if(data[0] == "1"){
//                    showMsg("telah diubah $msg")
                    cfPakarViewModel.update(data[1].toInt(), data[2].toDouble())
                }else{
//                    showMsg("telah dihapus $msg")
                    cfPakarViewModel.delete(data[1].toInt())

                }

                sDialog.dismissWithAnimation()
            }
            .setCancelButton("Batal") { ssDialog -> ssDialog.dismissWithAnimation() }
            .show()
    }

    override fun alertSuccess(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Berhasil") //
            .setContentText(msg)
            .setConfirmClickListener { sDialog ->

                sDialog.dismissWithAnimation()
            }
            .show()
    }

}