package com.androar.fitly
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.master.exoplayer.MasterExoPlayer


class RecyclerVideoAdapter(
    val videosList: ArrayList<String>
) : RecyclerView.Adapter<RecyclerVideoAdapter.ViewHolder>() {

    private val limit = 10


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerVideoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.androar.fitly.R.layout.item_rv_videos, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerVideoAdapter.ViewHolder, position: Int) {
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

            val videoView = itemView.findViewById<MasterExoPlayer>(com.androar.fitly.R.id.videoView)
            videoView.url = item
            videoView.requestFocus()
        }
    }
}