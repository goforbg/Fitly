package com.androar.fitly

import com.google.gson.JsonObject
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @GET("home_activities.json")
    fun getActivities(): Call<List<RecyclerItemActivities>>

    @GET("/access_token?")
    fun getToken(
        @Query ("channel") channel: String?,
        @Query("uid") uid: String?
    ): Call<JsonObject>?

}