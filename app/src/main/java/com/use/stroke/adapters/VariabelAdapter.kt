package com.use.stroke.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.powerspinner.PowerSpinnerView
import com.use.stroke.R
import com.use.stroke.datas.CFPakarData
import com.use.stroke.models.Laporan
import java.lang.Exception

class VariabelAdapter(private val context: Context, private val count:Int): RecyclerView.Adapter<VariabelAdapter.VariabelViewlHolder>() {

    companion object{
        const val TAG = "VariabelAdapter"
    }

    var itemSelectVariabel = ArrayList<String>()
    var itemSelectCf = ArrayList<Double>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariabelViewlHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_variabel, parent, false)
        return VariabelViewlHolder(view)
    }

    override fun onBindViewHolder(holder: VariabelViewlHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder $position")

        holder.bind(position)

    }

    override fun getItemCount(): Int {

        return count
    }

    inner class VariabelViewlHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val spinner : PowerSpinnerView = itemView.findViewById(R.id.item_variabel_spinner)
        val name : EditText = itemView.findViewById(R.id.item_variabel_name)

        val dataList = CFPakarData().getDataCFPakar()

        fun bind(position: Int) {

            val listSpinner = mutableListOf<String>()
            dataList.forEach {
                listSpinner.add(it.name)
            }
            spinner.apply {
                setItems(listSpinner)
                setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
                    val value = dataList[index]

                    val valueName = name.text.trim().toString()
                    if(valueName.isEmpty()) {
                        showMsg("Silahkan Mengisi Variabel Terlebih dahulu")
                        spinner.clearSelectedItem()
                    }else{
//                        showMsg("Index Terpilih ${value.value}")
                        itemSelectVariabel.add(valueName)
                        itemSelectCf.add(value.value)
                    }

                }
            }
        }
    }

    private fun showMsg(msg:String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}