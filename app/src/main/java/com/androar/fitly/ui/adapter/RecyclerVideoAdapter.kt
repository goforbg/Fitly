package com.androar.fitly.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androar.fitly.R
import com.master.exoplayer.MasterExoPlayer


class RecyclerVideoAdapter(
    val videosList: ArrayList<String>
) : RecyclerView.Adapter<RecyclerVideoAdapter.ViewHolder>() {

    private val limit = 10


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_videos, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(videosList[position])
    }

    override fun getItemCount(): Int {
        if(videosList.size > limit){
            return limit;
        }
        else
        {
            return videosList.size
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: String) {

            val videoView = itemView.findViewById<MasterExoPlayer>(R.id.videoView)
            videoView.url = item
            videoView.requestFocus()
        }
    }
}