package com.use.stroke.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.use.stroke.R
import com.use.stroke.ResearchDetailActivity
import com.use.stroke.ResearchFormActivity
import com.use.stroke.models.Research
import com.use.stroke.utils.Constants
import kotlinx.android.synthetic.main.item_research.view.*

class ResearchAdapter(private val context: Context): RecyclerView.Adapter<ResearchAdapter.ResearchViewlHolder>() {

    companion object{
        const val TAG = "ResearchAdapter"
    }
    private var dataList = mutableListOf<Research>()
    private lateinit var onItemDeleteClickCallback: OnItemDeleteClickCallback

    fun setList(data: MutableList<Research>){
        dataList.clear()
        dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResearchViewlHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_research, parent, false)

        return ResearchViewlHolder(view)
    }

    override fun onBindViewHolder(holder: ResearchViewlHolder, position: Int) {
        holder.bindView(dataList[position])
    }

    override fun getItemCount(): Int {
        return if(dataList.size>0) dataList.size else 0
    }

    inner class ResearchViewlHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindView(research: Research){
            itemView.item_research_title.text = research.title
            itemView.item_research_desc.text = "\t\t\t\t\t\t${research.desc.replace("//","")}"

            itemView.item_research_read.setOnClickListener {
                context.startActivity(Intent(context, ResearchDetailActivity::class.java).apply {
                    putExtra("id", research.id)
                })
            }
            if(Constants.getRole(context)==1) {
                itemView.item_research_delete.visibility = View.VISIBLE
                itemView.item_research_delete.setOnClickListener {
                    onItemDeleteClickCallback.onItemClick(research)
                    true
                }

                itemView.setOnClickListener {
                    context.startActivity(Intent(context, ResearchFormActivity::class.java).apply {
                        putExtra("id", research.id)
                        putExtra("title", research.title)
                        putExtra("desc", research.desc)
                        putExtra("status", "Update")
                    })
                }
            }else{
                itemView.item_research_delete.visibility = View.INVISIBLE
            }


        }
    }

    fun setOnItemDeleteClickCallback(onItemDeleteClickCallback: OnItemDeleteClickCallback) {
        this.onItemDeleteClickCallback = onItemDeleteClickCallback
    }

    interface OnItemDeleteClickCallback {
        fun onItemClick(data: Research)
    }
}