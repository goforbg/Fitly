package com.androar.fitly.network

import com.androar.fitly.model.ExcercisesListClass
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @GET("home_activities.json")
    fun getActivities(): Call<List<ExcercisesListClass>>

    @GET("/access_token?")
    fun getToken(
        @Query ("channel") channel: String?,
        @Query("uid") uid: String?
    ): Call<JsonObject>?

}