package com.structure.base_mvvm.domain.groups.entity


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.structure.base_mvvm.domain.utils.dayName
import com.structure.base_mvvm.domain.utils.time


data class Scheduled(
  @SerializedName("end_time")
  @Expose
  val endTime: String = "",
  @SerializedName("id")
  @Expose
  val id: Int = 0,
  @SerializedName("scheduled_date")
  @Expose
  val scheduledDate: String = "",
  @SerializedName("start_time")
  @Expose
  val startTime: String = ""
) {
  val scheduled
    get() = scheduledDate.dayName(scheduledDate)
  val start
    get() = startTime.time(startTime)
  val end
    get() = endTime.time(endTime)

}
