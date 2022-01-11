package com.structure.base_mvvm.domain.educational.use_case

import com.structure.base_mvvm.domain.educational.entity.Stage
import com.structure.base_mvvm.domain.educational.repository.EducationalRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EducationalUseCase @Inject constructor(
  private val educationalRepository: EducationalRepository
) {
  operator fun invoke(): Flow<Resource<BaseResponse<List<Stage>>>> =
    flow {
      emit(Resource.Loading)
      val result = educationalRepository.educationalStages()
      emit(result)
    }.flowOn(Dispatchers.IO)

}