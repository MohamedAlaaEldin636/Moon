package com.structure.base_mvvm.domain.groups.entity


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.structure.base_mvvm.domain.home.models.Instructor

data class GroupDetails(
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
  @SerializedName("instructor")
  @Expose
  val instructor: Instructor = Instructor(),
  @SerializedName("name")
  @Expose
  val name: String = "",
  @SerializedName("scheduled")
  @Expose
  val scheduled: List<Scheduled> = listOf()
)