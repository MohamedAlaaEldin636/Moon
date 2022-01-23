package com.structure.base_mvvm.domain.student_teacher.entity

import androidx.annotation.Keep
import com.structure.base_mvvm.domain.home.models.Instructor
import com.structure.base_mvvm.domain.home.models.Slider
import com.structure.base_mvvm.domain.home.models.Subject

@Keep
data class StudentTeacher(
  val subjects: ArrayList<Subject> = ArrayList(),
  val instructors: ArrayList<Instructor> = ArrayList(),
  val sliders: List<Slider> = listOf()
)