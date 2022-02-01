package app.grand.tafwak.domain.teacher_profile.use_case

import app.grand.tafwak.domain.home.models.Instructor
import app.grand.tafwak.domain.teacher_profile.repository.TeacherProfileRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TeacherProfileUseCase @Inject constructor(
  private val teacherProfileRepository: TeacherProfileRepository
) {
  operator fun invoke(instructor_id: Int): Flow<Resource<BaseResponse<Instructor>>> =
    flow {
      emit(Resource.Loading)
      val result = teacherProfileRepository.teacherProfile(instructor_id)

      emit(result)
    }.flowOn(Dispatchers.IO)

}