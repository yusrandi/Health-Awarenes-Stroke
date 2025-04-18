package com.use.steps.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.use.steps.DetailFastActivity
import com.use.steps.R
import com.use.steps.models.Laporan
import kotlinx.android.synthetic.main.item_fast.view.*

class FastAdapter(private val context: Context): RecyclerView.Adapter<FastAdapter.FastViewlHolder>() {

    companion object{
        const val TAG = "fastAdapter"
        var laporanValue : Laporan? = null
    }

    private var dataList = mutableListOf<Laporan>()

    fun setList(data: MutableList<Laporan>){
        dataList.clear()
        dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastViewlHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_fast, parent, false)
        return FastViewlHolder(view)
    }

    override fun onBindViewHolder(holder: FastViewlHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder $position")

        holder.bind(position)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")

        return dataList.size
    }

    inner class FastViewlHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) {

            Log.d(TAG, "pos $position")

            val laporan = dataList[position]

            itemView.item_fast_title.text = laporan.user.name
            itemView.item_fast_desc.text = "Hasil Konsultasi yang telah dilakukan, probabilitas ${laporan.result}%"

            itemView.setOnClickListener {
                laporanValue = laporan
                context.startActivity(Intent(context, DetailFastActivity::class.java))
            }

        }
    }
}