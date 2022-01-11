package com.structure.base_mvvm.domain.countries.use_case

import com.structure.base_mvvm.domain.countries.entity.Country
import com.structure.base_mvvm.domain.countries.repository.CountriesRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CountriesUseCase @Inject constructor(
  private val countriesRepository: CountriesRepository
) {
  operator fun invoke(): Flow<Resource<BaseResponse<List<Country>>>> =
    flow {
      emit(Resource.Loading)
      val result = countriesRepository.countries()
      emit(result)
    }.flowOn(Dispatchers.IO)

}