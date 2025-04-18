package com.use.steps.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.use.steps.R
import com.use.steps.ResultActivity
import kotlinx.android.synthetic.main.item_result.view.*

class ResultAdapter(private val context: Context, private val list: MutableList<ResultActivity.ItemResult>): RecyclerView.Adapter<ResultAdapter.FastViewlHolder>() {

    companion object{
        const val TAG = "fastAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastViewlHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_result, parent, false)
        return FastViewlHolder(view)
    }

    override fun onBindViewHolder(holder: FastViewlHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemCount(): Int {

        return list.size
    }

    inner class FastViewlHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) {
            val item = list[position]

            itemView.item_result_soal.text = item.soal
            itemView.item_result_response.text = item.response
            itemView.item_result_jawaban.text = item.jawaban
        }
    }
}