package com.zkmsz.a7minuteworkout

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_exercise_status.view.*

class ExerciseStatuAdapter(val context: Context,val items:ArrayList<ExerciseModel>): RecyclerView.Adapter<ExerciseStatuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercise_status,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val model = items[position]
        holder.tvItem.text = model.getId().toString()

        if (model.getIsselected()) {
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circuler_thin_color_accent_border)
            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        } else if (model.getIsCompleted()) {
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circular_color_accent_background)
            holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circular_color_gray_background)
            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        }
    }


    class ViewHolder(view:View): RecyclerView.ViewHolder(view)
    {
        var tvItem= view.tvItem
    }

}