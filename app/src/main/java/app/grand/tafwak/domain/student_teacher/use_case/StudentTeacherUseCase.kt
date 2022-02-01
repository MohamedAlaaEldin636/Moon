package app.grand.tafwak.domain.student_teacher.use_case

import app.grand.tafwak.domain.student_teacher.entity.StudentTeacher
import app.grand.tafwak.domain.student_teacher.repository.StudentTeacherRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
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