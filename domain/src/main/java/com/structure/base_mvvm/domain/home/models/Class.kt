package com.structure.base_mvvm.domain.home.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Class(
    @SerializedName("allowed_gender")
    @Expose
    val allowedGender: String = "",
    @SerializedName("class_type")
    @Expose
    val classType: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("name")
    @Expose
    val name: String = ""
)