package com.structure.base_mvvm.domain.student_teacher.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Paginate
import com.structure.base_mvvm.domain.home.models.Instructor

data class InstructorMain(
  @SerializedName("data")
  var instructor: List<Instructor> = listOf()
) : Paginate()
