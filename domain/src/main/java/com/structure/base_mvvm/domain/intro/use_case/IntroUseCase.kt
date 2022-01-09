package com.structure.base_mvvm.domain.intro.use_case

import com.structure.base_mvvm.domain.intro.entity.AppTutorial
import com.structure.base_mvvm.domain.intro.repository.IntroRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class IntroUseCase @Inject constructor(
  private val introRepository: IntroRepository) {

  operator fun invoke(): Flow<Resource<BaseResponse<List<AppTutorial>>>> = flow {
    emit(Resource.Loading)
    val result = introRepository.intro()
    emit(result)
  }.flowOn(Dispatchers.IO)
}