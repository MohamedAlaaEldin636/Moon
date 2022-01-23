package com.structure.base_mvvm.domain.home.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Instructor(
    @SerializedName("account_type")
    @Expose
    val accountType: String = "",
    @SerializedName("email")
    @Expose
    val email: String = "",
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("image")
    @Expose
    val image: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("nickname")
    @Expose
    val nickname: String = "",
    @SerializedName("phone")
    @Expose
    val phone: String = "",
    @SerializedName("register_steps")
    @Expose
    val registerSteps: Int = 0,
    @SerializedName("token")
    @Expose
    val token: String = ""
)