package com.structure.base_mvvm.domain.student_teacher.repository

import com.structure.base_mvvm.domain.student_teacher.entity.StudentTeacher
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface StudentTeacherRepository {
  suspend fun studentTeacher(): Resource<BaseResponse<StudentTeacher>>

}