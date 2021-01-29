package com.use.stroke.adapters

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.use.stroke.R
import com.use.stroke.models.History
import com.use.stroke.models.Research
import kotlinx.android.synthetic.main.item_fast.view.*
import kotlinx.android.synthetic.main.item_pembimbing_home.view.*

class PembimbingHomeAdapter(private val context: Context): RecyclerView.Adapter<PembimbingHomeAdapter.PembimbingHomeViewlHolder>() {

    companion object{
        const val TAG = "PembimbingHomeAdapter"
    }

    private var dataList = mutableListOf<History>()

    fun setList(data: MutableList<History>){
        dataList.clear()
        dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PembimbingHomeViewlHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pembimbing_home, parent, false)
        return PembimbingHomeViewlHolder(view)
    }

    override fun onBindViewHolder(holder: PembimbingHomeViewlHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder $position")

        holder.bind(position)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")

        return dataList.size
    }

    inner class PembimbingHomeViewlHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) {
            Log.d(TAG, "pos $position")
            val history = dataList[position]

            itemView.item_pembimbing_home_event.text = Html.fromHtml("<b>${history.user!!.name}</b> ${history.event}")
            itemView.item_pembimbing_home_date.text = history.date

        }
    }
}