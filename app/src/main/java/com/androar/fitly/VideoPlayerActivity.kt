package com.androar.fitly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.master.exoplayer.MasterExoPlayerHelper


class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.androar.fitly.R.layout.activity_video_player)

        var videosList : ArrayList<String> = arrayListOf()
        videosList.add("https://v16.muscdn.com/7f1bb148f117fe41ca0284ed9ea3b22f/5e9863c7/video/tos/useast2a/tos-useast2a-ve-0068c001/dc1a2ba223a647909142686f019a834b/?a=1233&br=1276&bt=638&cr=0&cs=0&dr=0&ds=6&er=&l=2020041607550601018907203435183787&lr=musically&qs=5&rc=M25rOHZ1c2ZldDMzZDczM0ApNDc8Ojs7ZTw8NzRmNGlmNmdxMnNqZmNkcl9fLS01MTZzcy5jYjY2LWEtNjNfYTM1YDI6Yw%3D%3D&vl=&vr=")
        videosList.add("https://v19.muscdn.com/25fb825a6ffe35c82b29bcd5a5034bec/5e9868aa/video/tos/useast2a/tos-useast2a-ve-0068c003/698ddce4253349ac9f548f5cc014807b/?a=1233&br=1544&bt=772&cr=0&cs=0&dr=0&ds=6&er=&l=202004160815110101890740383C1764A6&lr=musically&qs=5&rc=M210ZGpuMzV0dDMzaDczM0ApaTo3aDdnN2VkN2Y5OjRnaGc1Ni1fX19qXjVfLS01MTZzc2JfYS9fYS9jXi1eXi9hMTA6Yw%3D%3D&vl=&vr=")
        videosList.add("https://v16.muscdn.com/05d12749aa3c580bba9c2fea12e7991b/5e986893/video/tos/useast2a/tos-useast2a-pve-0068/f16280d55b3148e1b8bd1ccc904047cb/?a=1233&br=1598&bt=799&cr=0&cs=0&dr=0&ds=6&er=&l=20200416081537010189066018531B219D&lr=musically&qs=5&rc=M215cXZnPHQ6dDMzNTczM0ApaGVoaDc5M2Q6NzQ1ZDs5OWdeaS9mNS5uLS1fLS0wMTZzcy40LjYvLTJjLS01Y2FhMTU6Yw%3D%3D&vl=&vr=")
        videosList.add("https://v16.muscdn.com/acc360e71ea840ad5d3fd6d177a2aed1/5e9868bc/video/tos/useast2a/tos-useast2a-ve-0068c003/d9f41df7223e4192a3d6560e96078f3a/?a=1233&br=1322&bt=661&cr=0&cs=0&dr=0&ds=6&er=&l=202004160815430101890740363F140739&lr=musically&qs=5&rc=MzZzNzxrb3JzdDMzOzczM0ApZzdnO2k1NmU2N2QzaDloNmdwMG1icHFkLy5fLS02MTZzczMwMTAtMWNiMmEyNF9hYmE6Yw%3D%3D&vl=&vr=")

        val recyclerView = findViewById<RecyclerView>(R.id.rvVideos)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(recyclerView)

        val masterExoPlayerHelper = MasterExoPlayerHelper(this, id = R.id.videoView, autoPlay = true)
        masterExoPlayerHelper.makeLifeCycleAware(this)
        masterExoPlayerHelper.attachToRecyclerView(recyclerView)

        //Used to customize attributes
        masterExoPlayerHelper.getPlayerView().apply {
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }


        val adapter = RecyclerVideoAdapter(videosList)
        recyclerView.adapter = adapter



    }
}
