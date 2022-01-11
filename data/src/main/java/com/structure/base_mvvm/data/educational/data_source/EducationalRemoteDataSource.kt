package com.structure.base_mvvm.data.educational.data_source

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class EducationalRemoteDataSource @Inject constructor(private val apiService: EducationalServices) :
  BaseRemoteDataSource() {

  suspend fun educationalStages() = safeApiCall {
    apiService.educationalStages()
  }
}