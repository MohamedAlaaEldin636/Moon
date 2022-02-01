package app.grand.tafwak.data.teacher_profile.data_source

import app.grand.tafwak.domain.home.models.Instructor
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TeacherProfileServices {
  @GET("v1/student/instructors/{instructorId}")
  suspend fun teacherProfile(@Path("instructorId") instructorId: Int): BaseResponse<Instructor>

}