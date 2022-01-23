package com.structure.base_mvvm.domain.teacher_profile.entity

import androidx.annotation.Keep
import com.structure.base_mvvm.domain.home.models.Instructor

@Keep
data class TeacherProfile(
  val instructor: Instructor = Instructor()
)