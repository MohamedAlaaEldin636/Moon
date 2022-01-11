package com.structure.base_mvvm.data.countries.repository

import com.structure.base_mvvm.data.countries.data_source.CountriesRemoteDataSource
import com.structure.base_mvvm.domain.countries.entity.Country
import com.structure.base_mvvm.domain.countries.repository.CountriesRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
  private val remoteDataSource: CountriesRemoteDataSource
) : CountriesRepository {
  override suspend fun countries(): Resource<BaseResponse<List<Country>>> = remoteDataSource.countries()
}