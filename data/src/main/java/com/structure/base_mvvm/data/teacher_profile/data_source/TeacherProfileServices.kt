package com.structure.base_mvvm.data.teacher_profile.data_source

import com.structure.base_mvvm.domain.home.models.Instructor
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TeacherProfileServices {
  @GET("v1/student/instructors/{instructorId}")
  suspend fun teacherProfile(@Path("instructorId") instructorId: Int): BaseResponse<Instructor>

}