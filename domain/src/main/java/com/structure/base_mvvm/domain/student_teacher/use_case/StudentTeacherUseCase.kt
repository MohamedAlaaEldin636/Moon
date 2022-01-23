package com.structure.base_mvvm.domain.student_teacher.use_case

import com.structure.base_mvvm.domain.student_teacher.entity.StudentTeacher
import com.structure.base_mvvm.domain.student_teacher.repository.StudentTeacherRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StudentTeacherUseCase @Inject constructor(
  private val studentTeacherRepository: StudentTeacherRepository
) {
  operator fun invoke(): Flow<Resource<BaseResponse<StudentTeacher>>> =
    flow {
      emit(Resource.Loading)
      val result = studentTeacherRepository.studentTeacher()

      emit(result)
    }.flowOn(Dispatchers.IO)

}