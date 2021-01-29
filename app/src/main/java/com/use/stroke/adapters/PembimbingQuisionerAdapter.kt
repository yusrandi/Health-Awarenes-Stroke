package com.use.stroke.adapters

import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.use.stroke.QuisionerFormActivity
import com.use.stroke.R
import com.use.stroke.models.History
import com.use.stroke.models.Quisioner
import com.use.stroke.models.QuisionerResponse
import com.use.stroke.models.Research
import kotlinx.android.synthetic.main.item_fast.view.*
import kotlinx.android.synthetic.main.item_pembimbing_home.view.*

class PembimbingQuisionerAdapter(private val context: Context): RecyclerView.Adapter<PembimbingQuisionerAdapter.PembimbingQuisionerViewlHolder>() {

    companion object{
        const val TAG = "PembimbingQuisionerAdapter"
    }

    private var dataList = mutableListOf<QuisionerResponse>()

    fun setList(data: MutableList<QuisionerResponse>){
        dataList.clear()
        dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PembimbingQuisionerViewlHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pembimbing_home, parent, false)
        return PembimbingQuisionerViewlHolder(view)
    }

    override fun onBindViewHolder(holder: PembimbingQuisionerViewlHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder $position")

        holder.bind(position)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")

        return dataList.size
    }

    inner class PembimbingQuisionerViewlHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) {
            Log.d(TAG, "pos $position")
            val quisionerResponse = dataList[position]

            itemView.item_pembimbing_home_event.text = Html.fromHtml("<b>Anda </b> telah mengisi Quisioner")
            itemView.item_pembimbing_home_date.text = quisionerResponse.date
            itemView.setOnClickListener {
                context.startActivity(Intent(context, QuisionerFormActivity::class.java).apply {
                    putExtra("response", quisionerResponse.response)
                })
            }

        }
    }
}