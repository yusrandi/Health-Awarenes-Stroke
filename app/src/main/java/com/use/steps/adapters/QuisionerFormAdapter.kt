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
import com.use.steps.models.Quisioner
import java.lang.Exception

class QuisionerFormAdapter(private val context: Context) :
    RecyclerView.Adapter<QuisionerFormAdapter.QuisionerViewHolder>() {

    companion object {
        const val TAG = "QuisionerAdapter"
    }

    private var dataListSpinner = mutableListOf<String>()

    var itemSelect = ArrayList<Int>()
    var dataList = mutableListOf<Quisioner>()
    var dataListSelected = mutableListOf<Int>()

    fun setList(data: MutableList<Quisioner>) {
        dataList.clear()
        dataList = data

        notifyDataSetChanged()

    }
    fun setListSelected(data: MutableList<Int>) {
        dataListSelected.clear()
        dataListSelected = data

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuisionerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_konsultasi, parent, false)

        return QuisionerViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuisionerViewHolder, position: Int) {
        val Quisioner = dataList[position]
        holder.bin(Quisioner)

    }

    override fun getItemCount(): Int {
//        Log.e(TAG, "getItemCount ${dataList.size}")
        return if (dataList.size > 0) dataList.size else 0
    }

    private fun showMsg(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    inner class QuisionerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val tvName: TextView = itemView.findViewById(R.id.item_konsul_gejala)
        val spinner: PowerSpinnerView = itemView.findViewById(R.id.item_konsul_spinner)

        fun bin(Quisioner: Quisioner) {

//            Log.e(TAG, "position ${Quisioner.id}")

            dataListSpinner.clear()
            dataListSpinner.add("Ya")
            dataListSpinner.add("Tidak")

            tvName.text = Quisioner.name

            spinner.apply {
                setItems(dataListSpinner)

                setOnSpinnerItemSelectedListener<String> { _, _, index, _ ->
//                    showMsg("Index Terpilih $index")

                    try {
                        if (itemSelect.size == position)
                            itemSelect.add(index)
                        else {
                            itemSelect.removeAt(position)
                            itemSelect.add(position, index)
                        }
                    } catch (e: Exception) {
                        spinner.clearSelectedItem()
                        showMsg("Harap Memilih teratur")
                    }
                }
            }
            if(dataListSelected.size > 0 )spinner.selectItemByIndex(dataListSelected[position])


        }
    }

}
