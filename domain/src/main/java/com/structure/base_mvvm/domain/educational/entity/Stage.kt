package com.structure.base_mvvm.domain.educational.entity


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
data class Stage(
  @SerializedName("name")
  @Expose
  val name: String = "",
  @SerializedName("id")
  @Expose
  val id: Int = 1

)