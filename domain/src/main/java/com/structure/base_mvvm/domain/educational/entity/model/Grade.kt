package com.structure.base_mvvm.domain.educational.entity.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
data class Grade(
  @SerializedName("name")
  @Expose
  val name: String = "",
  @SerializedName("id")
  @Expose
  val id: Int = 1

)