package com.androar.fitly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerItemActivitesAdapter (val userList: List<RecyclerItemActivities>) : RecyclerView.Adapter<RecyclerItemActivitesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemActivitesAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_activities, parent, false)
            return ViewHolder(v)
    }



    override fun onBindViewHolder(holder: RecyclerItemActivitesAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: RecyclerItemActivities) {
            val tvActivityURL  = itemView.findViewById(R.id.ivItem) as ImageView
            Glide.with(itemView.rootView.context)
                .load(item.t)
                .placeholder(R.drawable.ic_icon)
                .into(tvActivityURL)

        }
    }
}