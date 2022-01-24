package com.structure.base_mvvm.domain.home.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.structure.base_mvvm.domain.reviews.entity.Reviews

@Keep
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
  val token: String = "",
  @SerializedName("average_rating")
  @Expose
  val average_rating: String = "0",
  @SerializedName("description")
  @Expose
  val description: String = "",
  @SerializedName("subject")
  @Expose
  val subject: Subject = Subject(),
  @SerializedName("classes")
  @Expose
  val classes: List<Classes> = listOf(),
  @SerializedName("reviews")
  @Expose
  val reviews: List<Reviews> = listOf(),


  )