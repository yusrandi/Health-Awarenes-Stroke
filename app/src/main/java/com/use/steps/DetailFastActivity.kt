package com.use.steps

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.use.steps.adapters.FastAdapter
import com.use.steps.adapters.ResultAdapter
import com.use.steps.models.Gejala
import com.use.steps.viewmodels.GejalaState
import com.use.steps.viewmodels.GejalaViewModel
import devlight.io.library.ArcProgressStackView
import kotlinx.android.synthetic.main.activity_detail_fast.*
import java.util.ArrayList

class DetailFastActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DetailFastActivity"
    }

    private lateinit var gejalaViewModel: GejalaViewModel
    private var listItem = mutableListOf<ResultActivity.ItemResult>()
    private var dataListCFUser = StepFastActivity.dataListCFUser

    var itemSelectAll = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_fast)

        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)
        gejalaViewModel.getState().observer(this) {
            Log.e(TAG, "gejalaViewModel.getState() $it")
            handleUIGejala(it)
        }
        FastAdapter.laporanValue.let {
            fast_detail_name.text = it!!.user.name
            initProgressArch(it.result.toFloat())
            detail_fast_persen.text = "${it.result}%"
            it.cfs.split(",").forEach { c ->
                itemSelectAll.add(c.toInt())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        gejalaViewModel.fetchGejalas()
    }

    private fun handleUIGejala(it: GejalaState) {

        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }

            is GejalaState.IsLoad -> {
                calculateCFCombine(it.data)
            }

            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun calculateCFCombine(it: List<Gejala>) {

        var CFOld = 0.0
        for (index in it.indices) {
            val dataSoal = it[index]
            val dataResponse = itemSelectAll[index]
            val dataJawaban = dataSoal.jawabans?.get(dataResponse)

            val dataSoalCFUser = dataListCFUser[index]
            val dataJawabanCFUser = dataSoalCFUser.cfusers?.get(dataResponse)




            val CF = dataJawaban!!.cf * dataJawabanCFUser!!.cf
            Log.e(TAG, "CFOld $CFOld, CF $CF")
            val result = (CFOld + CF) * (1 - CFOld)

            Log.e(TAG, "Soal ${dataSoal.name}, Response $dataResponse, Jawaban ${dataJawaban.jawaban.name}, CF Pakar ${dataJawaban.cf}, CF User ${dataJawabanCFUser.cf}, Result $result")

            CFOld = result

            listItem.add(
                ResultActivity.ItemResult(
                    "${index + 1}. ${dataSoal.name}",
                    dataJawaban.jawaban.name,
                    "CF Pakar (${dataJawaban.cf}) * CF User (${dataJawabanCFUser.cf}), Hasil $result"
                )
            )


            fast_detail_rv.apply {
                adapter = ResultAdapter(this@DetailFastActivity, listItem)
                layoutManager = LinearLayoutManager(this@DetailFastActivity)
            }
        }

    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_detail_fast.visibility = View.VISIBLE
        } else {
            spin_kit_detail_fast.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun initProgressArch(value: Float) {
        val modelCount = 4

        // Parsed colors
        val mStartColors = IntArray(modelCount)
        val mEndColors = IntArray(modelCount)


        val mArcProgressStackView: ArcProgressStackView = findViewById(R.id.apsv_detail_fast)
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
                value,
                R.color.colorPrimaryDark,
                R.color.colorAccent
            )
        )
//        models.add(ArcProgressStackView.Model("Perencanaan", 50f, R.color.colorPrimary, mStartColors[0]))
        mArcProgressStackView.models = models
    }
}