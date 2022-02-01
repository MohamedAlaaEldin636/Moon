package app.grand.tafwak.domain.student_teacher.entity

import androidx.annotation.Keep
import app.grand.tafwak.domain.home.models.Slider
import app.grand.tafwak.domain.home.models.Subject

@Keep
data class StudentTeacher(
  val subjects: ArrayList<Subject> = ArrayList(),
  val instructors: InstructorMain = InstructorMain(),
  val sliders: List<Slider> = listOf()
)