package app.grand.tafwak.data.studentTeacher.data_source

import app.grand.tafwak.domain.student_teacher.entity.StudentTeacher
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.GET

interface StudentTeacherServices {
  @GET("v1/student/instructors")
  suspend fun studentTeacher(): BaseResponse<StudentTeacher>

}