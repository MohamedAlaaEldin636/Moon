package com.structure.base_mvvm.domain.home.models


import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Entity
data class HomeStudentData(
  @SerializedName("classes")
  @Expose
  val classes: List<Classes> = listOf(),
  @SerializedName("instructors")
  @Expose
  val instructors: List<Instructor> = ArrayList(),
  @SerializedName("notification_count")
  @Expose
  val notificationCount: Int = 0,
  @SerializedName("sliders")
  @Expose
  val sliders: List<Slider> = listOf()
)