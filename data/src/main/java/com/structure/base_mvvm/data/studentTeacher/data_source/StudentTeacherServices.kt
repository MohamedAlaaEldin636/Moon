package com.structure.base_mvvm.data.studentTeacher.data_source

import com.structure.base_mvvm.domain.student_teacher.entity.StudentTeacher
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.GET

interface StudentTeacherServices {
  @GET("v1/student/instructors")
  suspend fun studentTeacher(): BaseResponse<StudentTeacher>

}