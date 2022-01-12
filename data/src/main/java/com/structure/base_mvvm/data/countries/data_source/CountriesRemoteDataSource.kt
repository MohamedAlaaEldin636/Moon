package com.structure.base_mvvm.data.countries.data_source

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import com.structure.base_mvvm.domain.countries.entity.request.RegisterStep2
import javax.inject.Inject

class CountriesRemoteDataSource @Inject constructor(private val apiService: CountriesServices) :
  BaseRemoteDataSource() {

  suspend fun countries() = safeApiCall {
    apiService.countries()
  }

  suspend fun registerStep2(registerStep3: RegisterStep2) = safeApiCall {
    apiService.registerStep2(registerStep3)
  }

}