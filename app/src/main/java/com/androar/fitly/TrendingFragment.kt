package com.androar.fitly

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.master.exoplayer.MasterExoPlayerHelper
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Trending fragment that plays trending videos in a Tiktok fashion
 */
class TrendingFragment : Fragment() {

    lateinit var adapter : RecyclerVideoAdapter
    lateinit var recyclerView : RecyclerView

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trending, container, false)


        var videosList : ArrayList<String> = arrayListOf()
        videosList.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        videosList.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")

        recyclerView = view!!.findViewById<RecyclerView>(R.id.rvVideos)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(recyclerView)

        val masterExoPlayerHelper = MasterExoPlayerHelper(activity!!, id = R.id.videoView, autoPlay = true)
        masterExoPlayerHelper.makeLifeCycleAware(this)
        masterExoPlayerHelper.attachToRecyclerView(recyclerView)

        //Used to customize attributes
        masterExoPlayerHelper.getPlayerView().apply {
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }

        adapter = RecyclerVideoAdapter(videosList)
        recyclerView.adapter = adapter

        return view
    }

}
