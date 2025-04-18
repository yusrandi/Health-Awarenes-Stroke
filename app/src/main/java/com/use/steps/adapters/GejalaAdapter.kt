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
import com.use.steps.datas.CFUserData
import com.use.steps.models.Gejala
import java.lang.Exception

class GejalaAdapter(private val context: Context, private var dataList: MutableList<Gejala>) : RecyclerView.Adapter<GejalaAdapter.GejalaViewHolder>() {

    companion object {
        const val TAG = "GejalaAdapter"
    }

//    private var dataList = mutableListOf<Gejala>()
    private var dataListSpinner = mutableListOf<String>()
    private var dataListSpinnerYakin = mutableListOf<String>()

    var itemSelect = ArrayList<Int>()
    var itemSelectKeyakinan = ArrayList<Int>()

    fun setList(data: MutableList<Gejala>) {
        dataList.clear()
        dataList = data

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GejalaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_konsultasi, parent, false)

        dataListSpinnerYakin.clear()

        CFUserData().getDataCFUser().forEach {
            dataListSpinnerYakin.add(it.name)
        }
        return GejalaViewHolder(view)
    }

    override fun onBindViewHolder(holder: GejalaViewHolder, position: Int) {
        val gejala = dataList[position]
        holder.bin(gejala)

    }

    override fun getItemCount(): Int {
//        Log.e(TAG, "getItemCount ${dataList.size}")
        return if (dataList.size > 0) dataList.size else 0
    }

    private fun showMsg(msg:String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    inner class GejalaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val tvName : TextView = itemView.findViewById(R.id.item_konsul_gejala)
        val spinner : PowerSpinnerView = itemView.findViewById(R.id.item_konsul_spinner)

        fun bin(gejala: Gejala) {

//            Log.e(TAG, "position ${gejala.id}")

            dataListSpinner.clear()
            gejala.jawabans!!.forEach {
                dataListSpinner.add(it.jawaban.name)
            }
            tvName.text = gejala.name
            spinner.apply {
                setItems(dataListSpinner)
                setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
//                    showMsg("Index Terpilih $index")

                    try {
                        if(itemSelect.size == position)
                            itemSelect.add(index)
                        else{
                            itemSelect.removeAt(position)
                            itemSelect.add(position,index)
                        }

                    }catch (e:Exception){
                        showMsg("Harap Memilih teratur")
                    }
                }
            }

        }
    }

}
