package com.androar.fitly.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.androar.fitly.R
import com.androar.fitly.model.ExcercisesListClass
import com.bumptech.glide.Glide

class ExcercisesListAdapter (val userList: List<ExcercisesListClass>) : RecyclerView.Adapter<ExcercisesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_excercises, parent, false)
            return ViewHolder(
                v
            )
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: ExcercisesListClass) {
            val tvActivityURL  = itemView.findViewById(R.id.ivItem) as ImageView
            Glide.with(itemView.rootView.context)
                .load(item.t)
                .placeholder(R.drawable.ic_icon)
                .into(tvActivityURL)

        }
    }
}