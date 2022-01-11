package com.structure.base_mvvm.data.countries.data_source

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class CountriesRemoteDataSource @Inject constructor(private val apiService: CountriesServices) :
  BaseRemoteDataSource() {

  suspend fun countries() = safeApiCall {
    apiService.countries()
  }
}