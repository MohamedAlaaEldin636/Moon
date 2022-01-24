package com.structure.base_mvvm.domain.teacher_profile.use_case

import com.structure.base_mvvm.domain.home.models.Instructor
import com.structure.base_mvvm.domain.teacher_profile.entity.TeacherProfile
import com.structure.base_mvvm.domain.teacher_profile.repository.TeacherProfileRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
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