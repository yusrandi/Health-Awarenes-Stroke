package com.use.steps

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.use.steps.adapters.ResultAdapter
import com.use.steps.models.Gejala
import com.use.steps.utils.Constants
import com.use.steps.viewmodels.*
import devlight.io.library.ArcProgressStackView
import kotlinx.android.synthetic.main.activity_doctor_manage_gejala_form.konsul_first_step_back
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.dialog_result.view.*
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ResultActivity"
    }

    private lateinit var gejalaViewModel: GejalaViewModel
    private lateinit var laporanViewModel: LaporanViewModel
    private lateinit var historyViewModel: HistoryViewModel

    private var listItem = mutableListOf<ItemResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)
        laporanViewModel = ViewModelProviders.of(this).get(LaporanViewModel::class.java)
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        laporanViewModel.getState().observe(this, Observer {
            Log.e(TAG, "laporanViewModel.getState $it")
        })
        gejalaViewModel.fetchGejalas()
        gejalaViewModel.getState().observer(this) {
            Log.e(TAG, "gejalaViewModel.getState() $it")
            handleUIGejala(it)
        }
        result_kalkulasi.setOnClickListener { showDialog() }
        result_back.setOnClickListener { startActivity(Intent(this, StepHomeActivity::class.java)).also { finish() } }

        Log.e(TAG, "Item Select ${KonsulFirstStepActivity.itemSelectAll}")

        result_video.setOnClickListener {
            startActivity(Intent(this, ViedeoMenuActivity::class.java)).also { finish() }
        }
        result_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, StepHomeActivity::class.java)).also { finish() }
    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_result.visibility = View.VISIBLE
        } else {
            spin_kit_result.visibility = View.GONE
        }
    }

    private fun handleUIGejala(it: GejalaState) {

        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }


            is GejalaState.IsLoad -> {
//                calculateCFCombine(it.data)
                calculateCFCombine(it.data)
            }
            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun calculateCFCombine(it: List<Gejala>) {

        var CFOld = 0.0
        var cfResult = 0.0
        for (i in 1..3) {
            Log.e(TAG,"====================================================== $i")

            var minCFPakar = 2.0
            var minCFUser = 2.0
            var maxCFPakar = 0.0
            var maxCFUser = 0.0
            for (index in it.indices) {

                val dataSoal = it[index]
                val dataResponse = KonsulFirstStepActivity.itemSelectAll[index]
                val dataJawaban = dataSoal.jawabans?.get(dataResponse)

                val dataSoalCFUser = KonsulFirstStepActivity.dataListCFUser[index]
                val dataJawabanCFUser = dataSoalCFUser.cfusers?.get(dataResponse)

                val CF = dataJawaban!!.cf * dataJawabanCFUser!!.cf

                if(i == dataSoal.kategori_id){
                    Log.e(TAG, "Data Response $dataResponse")
                    Log.e(TAG, "CFOld $CFOld, CF $CF")
                    val result = (CFOld + CF) * (1 - CFOld)

                    Log.e(TAG, "No $index Kat ${dataSoal.kategori_id} Soal ${dataSoal.name}, Response $dataResponse, Jawaban ${dataJawaban.jawaban.name}, CF Pakar ${dataJawaban.cf}, CF User ${dataJawabanCFUser.cf}, Result $result")

//                    CFOld = result

                    if(i != 3){
                        if(dataJawaban.cf < minCFPakar) minCFPakar = dataJawaban.cf
                        if(dataJawabanCFUser.cf < minCFUser) minCFUser = dataJawabanCFUser.cf

                    }else{
                        if(dataJawaban.cf > maxCFPakar) maxCFPakar = dataJawaban.cf
                        if(dataJawabanCFUser.cf > maxCFUser) maxCFUser = dataJawabanCFUser.cf
                    }



                    listItem.add(
                        ItemResult(
                            "${index + 1}. ${dataSoal.name}",
                            dataJawaban.jawaban.name,
                            "CF Pakar (${dataJawaban.cf}) * CF User (${dataJawabanCFUser.cf}), Hasil $result"
                        )
                    )


                }


            }

            var newCF = 0.0
            if (i != 3) {
                Log.e(TAG, "Min CF Pakar $minCFPakar, Min CF User $minCFUser Kategori $i")
                newCF = minCFPakar * minCFUser
                if(i==2){
                    cfResult = CFOld+newCF-(CFOld*newCF)
                    CFOld = cfResult
                }else{
                    CFOld = newCF
                }
            } else {
                newCF = maxCFPakar * maxCFUser
                    cfResult = CFOld+newCF-(CFOld*newCF)

                Log.e(TAG, "Max CF Pakar $maxCFPakar, Max CF User $maxCFUser Kategori $i, CF Old $CFOld, new CF $newCF")

                CFOld = cfResult
            }


            Log.e(TAG, "CF_SA $newCF CF_RESULT $cfResult \n\n")

        }


        val sb = StringBuffer()

        for (i in 0 until KonsulFirstStepActivity.itemSelectAll.size) {
            if (i > 0) {
                sb.append(",")
            }
            sb.append(KonsulFirstStepActivity.itemSelectAll[i])
        }
        Log.e(TAG, "Result ${CFOld * 100}")
        val result = (cfResult * 100).toInt()
        initProgressArch(result)
        result_tv_persen.text = "$result%"

        if (result > 50) result_solusi.text = "Perbaiki Pola Diet, Olahraga, Jika ada faktor risiko konsumsi obat dan konsultasi ke dokter." else result_solusi.text = "Tetap Menjaga Pola diet dan olahraga, Kontrol teratur Kesehatan."


        if(Constants.getRole(this)==3){
            Constants.setResult(this, (CFOld * 100).toInt())
            laporanViewModel.store(Constants.getID(this), sb.toString(),(CFOld * 100).toInt().toString())
            val event = "Telah Melakukan Konsultasi dengan Hasil ${(CFOld * 100).toInt()}"
            historyViewModel.store(Constants.getID(this), event, getCurrentTime())
        }


    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("EEEE, dd MMMM HH:mm", Locale.getDefault()).format(Date())
    }
    private fun initProgressArch(persen: Int) {
        val modelCount = 4

        // Parsed colors
        val mStartColors = IntArray(modelCount)
        val mEndColors = IntArray(modelCount)


        var mArcProgressStackView: ArcProgressStackView = findViewById(R.id.apsv_result)
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
        models.add(ArcProgressStackView.Model("", persen.toFloat(), R.color.colorPrimaryDark, R.color.colorAccent))
//        models.add(ArcProgressStackView.Model("Perencanaan", 50f, R.color.colorPrimary, mStartColors[0]))
        mArcProgressStackView.models = models
    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_result, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
//            .setTitle("Hasil Kalkulasi")
        //show dialog

        val resultRv = mDialogView.findViewById(R.id.dialog_result_rv) as RecyclerView
        val resultAdapter = ResultAdapter(this, listItem)
        resultRv.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = resultAdapter
        }



        val mAlertDialog = mBuilder.show()

        mDialogView.dialog_result_back.setOnClickListener {
            mAlertDialog.dismiss()
        }
        //login button click of custom layout

    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    class ItemResult(var soal: String, var response:String, var jawaban: String)
}
