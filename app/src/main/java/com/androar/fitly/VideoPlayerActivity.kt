package com.androar.fitly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.master.exoplayer.MasterExoPlayerHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.androar.fitly.R.layout.activity_video_player)

        var videosList : ArrayList<String> = arrayListOf()
        videosList.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        videosList.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")

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

        loadDummyData()
        populateActivities()
    }

    fun loadDummyData() {
        val recyclerView = findViewById(R.id.rvExcercises) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val arrayList = ArrayList<RecyclerItemActivities>()
        arrayList.add(RecyclerItemActivities("", "", "", "", "", ""))
        arrayList.add(RecyclerItemActivities("", "", "", "", "", ""))
        arrayList.add(RecyclerItemActivities("", "", "", "", "", ""))
        val adapter = RecyclerItemActivitesAdapter(arrayList)
        recyclerView.adapter = adapter
    }
    private fun populateActivities() {
        val recyclerView = findViewById(R.id.rvExcercises) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        val arrayList = ArrayList<RecyclerItemActivities>()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://goforbg.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
        val call = api.getActivities()
        call.enqueue(object : Callback<List<RecyclerItemActivities>> {
            override fun onResponse(
                call: Call<List<RecyclerItemActivities>>,
                response: Response<List<RecyclerItemActivities>>
            ) {
                val activitesList = response.body()!!
                for (i in activitesList.indices) {
                    arrayList.add(
                        RecyclerItemActivities(
                            activitesList[i].n,
                            activitesList[i].t,
                            activitesList[i].i,
                            activitesList[i].e,
                            activitesList[i].v,
                            activitesList[i].d
                        )
                    )
                }
                val adapter = RecyclerItemActivitesAdapter(arrayList)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<RecyclerItemActivities>>, t: Throwable) {
                Log.d("RetrofitTest", t.toString())

                Toast.makeText(applicationContext, "Your internet is moo!", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

}
