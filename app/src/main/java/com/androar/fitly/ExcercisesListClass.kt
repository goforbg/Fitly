package com.androar.fitly

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ExcercisesListClass(@SerializedName("n")  @Expose open var n: String? = null  , @SerializedName("t")@Expose
open var t: String? = null  , @SerializedName("i") @Expose
open var i: String? = null  , @SerializedName("e") @Expose
open var e: String? = null  , @SerializedName("v")@Expose
open var v: String? = null  , @SerializedName("d")@Expose
open var d : String? = null  )
