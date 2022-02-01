package app.grand.tafwak.domain.student_teacher.entity

import com.google.gson.annotations.SerializedName
import app.grand.tafwak.domain.general.paginate.Paginate
import app.grand.tafwak.domain.home.models.Instructor

data class InstructorMain(
  @SerializedName("data")
  var instructor: List<Instructor> = listOf()
) : Paginate()
