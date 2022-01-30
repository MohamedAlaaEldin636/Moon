package com.structure.base_mvvm.domain.home.use_case

import com.structure.base_mvvm.domain.home.models.HomeStudentData
import com.structure.base_mvvm.domain.home.repository.HomeRepository
import com.structure.base_mvvm.domain.home.repository.local.HomeLocalRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class HomeUseCase @Inject constructor(
  private val homeRepository: HomeRepository,
  private val homeLocalRepository: HomeLocalRepository
) {
  operator fun invoke(): Flow<Resource<BaseResponse<HomeStudentData>>> =
    flow {
      emit(Resource.Loading)
      val result = homeRepository.studentHome()

      emit(result)
    }.flowOn(Dispatchers.IO)

  fun getHomeDataLocal() =
    flow {
      val result = homeLocalRepository.studentHomeLocal()
      emit(result)
      if (result)
    }.flowOn(Dispatchers.IO)
}