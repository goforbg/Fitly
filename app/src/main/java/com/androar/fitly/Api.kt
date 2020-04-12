package com.androar.fitly

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("home_activities.json")
    fun getActivities(): Call<List<RecyclerItemActivities>>
}