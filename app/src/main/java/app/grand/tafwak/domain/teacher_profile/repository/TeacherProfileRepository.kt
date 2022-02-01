package app.grand.tafwak.domain.teacher_profile.repository

import app.grand.tafwak.domain.home.models.Instructor
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface TeacherProfileRepository {
  suspend fun teacherProfile(instructor_id: Int): Resource<BaseResponse<Instructor>>

}