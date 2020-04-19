package com.androar.fitly

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

/**
 * A simple [Fragment] subclass.
 */
class TrendingFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var videosList : ArrayList<String> = arrayListOf()
        videosList.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        videosList.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")

        val recyclerView = view!!.findViewById<RecyclerView>(R.id.rvVideos)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(recyclerView)

        val masterExoPlayerHelper = MasterExoPlayerHelper(context!!, id = R.id.videoView, autoPlay = true)
        masterExoPlayerHelper.makeLifeCycleAware(this)
        masterExoPlayerHelper.attachToRecyclerView(recyclerView)

        //Used to customize attributes
        masterExoPlayerHelper.getPlayerView().apply {
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }

        val adapter = RecyclerVideoAdapter(videosList)
        recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trending, container, false)


        return view
    }

}
