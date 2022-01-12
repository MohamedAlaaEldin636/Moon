package com.structure.base_mvvm.data.countries.repository

import com.structure.base_mvvm.data.countries.data_source.CountriesRemoteDataSource
import com.structure.base_mvvm.domain.countries.entity.Country
import com.structure.base_mvvm.domain.countries.entity.request.RegisterStep2
import com.structure.base_mvvm.domain.countries.repository.CountriesRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
  private val remoteDataSource: CountriesRemoteDataSource
) : CountriesRepository {
  override suspend fun countries(): Resource<BaseResponse<List<Country>>> = remoteDataSource.countries()
  override suspend fun registerStep2(registerStep2: RegisterStep2): Resource<BaseResponse<*>> =
    remoteDataSource.registerStep2(registerStep2)

}