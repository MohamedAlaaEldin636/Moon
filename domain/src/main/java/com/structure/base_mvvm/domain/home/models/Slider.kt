package com.structure.base_mvvm.domain.home.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Slider(
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("image")
    @Expose
    val image: String = "",
    @SerializedName("type")
    @Expose
    val type: String = ""
)