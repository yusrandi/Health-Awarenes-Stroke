package com.use.steps.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.powerspinner.PowerSpinnerView
import com.use.steps.R
import com.use.steps.datas.CFPakarData
import com.use.steps.models.Gejala
import kotlinx.android.synthetic.main.item_konsultasi.view.*

class DoctorManageGejalaAdapter(private val context: Context) : RecyclerView.Adapter<DoctorManageGejalaAdapter.GejalaViewHolder>() {

    companion object {
        const val TAG = "GejalaAdapter"
    }

    //    private var dataList = mutableListOf<Gejala>()
    private var dataListSpinner = mutableListOf<String>()
    private var dataListSpinnerCf = mutableListOf<String>()

    var dataList = mutableListOf<Gejala>()
    var itemSelect = ArrayList<Int>()

    private lateinit var onItemUpdateClickCallback: OnItemUpdateClickCallback
    private lateinit var onItemDeleteClickCallback: OnItemDeleteClickCallback

    fun setList(data: MutableList<Gejala>) {
        dataList.clear()
        dataList = data

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorManageGejalaAdapter.GejalaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_konsultasi, parent, false)

        return GejalaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorManageGejalaAdapter.GejalaViewHolder, position: Int) {
        val gejala = dataList[position]
        holder.bin(gejala)

    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) dataList.size else 0
    }

    private fun showMsg(msg:String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    inner class GejalaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.findViewById(R.id.item_konsul_gejala)
        val tvAksiCancel : TextView = itemView.findViewById(R.id.item_konsul_aksi_cancel)
        val tvAksiUpdate : TextView = itemView.findViewById(R.id.item_konsul_aksi_update)
        val tvAksiDelete : TextView = itemView.findViewById(R.id.item_konsul_aksi_delete)
        val spinner : PowerSpinnerView = itemView.findViewById(R.id.item_konsul_spinner)
        val spinnerCf : PowerSpinnerView = itemView.findViewById(R.id.item_konsul_spinner_cf)

        fun bin(gejala: Gejala) {
            dataListSpinner.clear()
            dataListSpinnerCf.clear()

            var valueID = 0
            var valueCf = 0.0

            gejala.jawabans!!.forEach {
                dataListSpinner.add("${it.cf}, ${it.jawaban.name}")
            }
            tvName.text = gejala.name
            spinner.apply {
                setItems(dataListSpinner)
                setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                    val gejalaJawaban = gejala.jawabans[index]
//                    showMsg("Index Terpilih ${gejalaJawaban.id}")

                    valueID = gejalaJawaban.id!!

                   itemView.item_konsul_aksi.visibility = View.VISIBLE
                }
            }

            val data = CFPakarData().getDataCFPakar()
            data.forEach {
                dataListSpinnerCf.add(it.name)
            }
            spinnerCf.apply {
                setItems(dataListSpinnerCf)
                setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                    val dataValueCf = data[index]
//                    showMsg("Index Terpilih ${dataValueCf.value}")

                    valueCf = dataValueCf.value
                }
            }

            tvAksiCancel.setOnClickListener {
                itemView.item_konsul_aksi.visibility = View.GONE
                spinnerCf.clearSelectedItem()
                spinner.clearSelectedItem()

            }

            tvAksiUpdate.setOnClickListener {
                onItemUpdateClickCallback.onItemClick(valueID, valueCf)
                true
            }
            tvAksiDelete.setOnClickListener {
                onItemDeleteClickCallback.onItemDeleteClick(valueID)
                true
            }
        }
    }

    fun setOnItemUpdateClickCallback(onItemUpdateClickCallback: OnItemUpdateClickCallback) {
        this.onItemUpdateClickCallback = onItemUpdateClickCallback
    }

    interface OnItemUpdateClickCallback {
        fun onItemClick(id:Int, cf:Double)
    }

    fun setOnItemDeleteClickCallback(onItemDeleteClickCallback: OnItemDeleteClickCallback) {
        this.onItemDeleteClickCallback = onItemDeleteClickCallback
    }

    interface OnItemDeleteClickCallback {
        fun onItemDeleteClick(id: Int)
    }

}
