package app.grand.tafwak.domain.student_teacher.repository

import app.grand.tafwak.domain.student_teacher.entity.StudentTeacher
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface StudentTeacherRepository {
  suspend fun studentTeacher(): Resource<BaseResponse<StudentTeacher>>

}