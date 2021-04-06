package com.use.stroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.use.stroke.adapters.GejalaAdapter
import com.use.stroke.models.Gejala
import com.use.stroke.models.GejalaCFUser
import com.use.stroke.models.Laporan
import com.use.stroke.viewmodels.GejalaState
import com.use.stroke.viewmodels.GejalaViewModel
import kotlinx.android.synthetic.main.activity_konsul_first_step.*
import kotlinx.android.synthetic.main.activity_konsultasi_form.*
import java.util.*

class KonsulFirstStepActivity : AppCompatActivity() {

    companion object{
        const val TAG = "KonsulFirstStepActivity"
        var itemSelectAll = ArrayList<Int>()
        var dataListCFUser = mutableListOf<GejalaCFUser>()
    }
    private lateinit var gejalaAdapter: GejalaAdapter
    private lateinit var gejalaViewModel: GejalaViewModel

    private var katId = 0
    private var dataTitle = mutableListOf("First Step", "Second Step", "Third Step")
    private var dataSubTitle = mutableListOf("A. Faktor Risiko (KESEHATAN)", "B. Faktor Risiko (GAYA HIDUP)", "C. Gejala dan Ciri-Ciri Penderita Stroke?")

    var dataList = ArrayList<Gejala>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konsul_first_step)

        initRecyclerView()


        gejalaViewModel = ViewModelProviders.of(this).get(GejalaViewModel::class.java)
        gejalaViewModel.fetchGejalasByKategori(katId+1)
        gejalaViewModel.fetchGejalasCFUser()

        konsul_first_step_title.text = dataTitle[katId]
        konsul_first_step_subtitle.text = dataSubTitle[katId]

        gejalaViewModel.getState().observe(this, Observer {
            Log.e(TAG, "getState $it")
            handleUI(it)
        })

        itemSelectAll.clear()

        konsul_first_step_next.setOnClickListener {

            if(dataList.size == gejalaAdapter.itemSelect.size){

                Log.e(TAG, "Item Select ${gejalaAdapter.itemSelect}")

                gejalaAdapter.itemSelect.forEach {
                    itemSelectAll.add(it)
                }
                Log.e(TAG, "Kat ID ${katId+1}, data Size ${dataTitle.size}")
                if(katId+1 < dataTitle.size){

                    katId++
                    if(katId == 1) konsul_first_step_subtitle_keterangan.visibility = View.VISIBLE else konsul_first_step_subtitle_keterangan.visibility = View.GONE
                    gejalaViewModel.fetchGejalasByKategori(katId+1)
                    konsul_first_step_title.text = dataTitle[katId]
                    konsul_first_step_subtitle.text = dataSubTitle[katId]

                    initRecyclerView()

                }else{
                    konsul_first_step_next.text = "Get Result"
                    Log.e(TAG, "Item Select All $itemSelectAll")

                    startActivity(Intent(this, ResultActivity::class.java)).also { finish() }

                }
            }else{
                showMsg("Harap Mengisi Semua Pertanyaan")
            }

        }

    }



    private fun handleUI(it: GejalaState) {

        when (it) {

            is GejalaState.IsError -> {
                isLoading(false)
                showMsg(it.err)
            }
            is GejalaState.IsLoad -> {

                gejalaAdapter.setList(it.data)
                dataList.clear()
                dataList.addAll(it.data)
            }
            is GejalaState.IsLoadCFUser -> {
//                calculateCFCombine(it.data)
                dataListCFUser.clear()
                dataListCFUser.addAll(it.data)
            }
            is GejalaState.IsLoading -> isLoading(it.state)
            else -> showMsg("Undefined")
        }
    }

    private fun initRecyclerView() {

        gejalaAdapter = GejalaAdapter(this, mutableListOf())
        konsul_first_step_rv.apply {
            layoutManager = LinearLayoutManager(this@KonsulFirstStepActivity)
            adapter = gejalaAdapter
        }

    }

    private fun isLoading(state: Boolean) {

        if (state) {
            spin_kit_konsul_first_step.visibility = View.VISIBLE
        } else {
            spin_kit_konsul_first_step.visibility = View.GONE
        }
    }

    private fun showMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

}