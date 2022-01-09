package com.structure.base_mvvm.data.intro.data_source

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class IntroRemoteDataSource @Inject constructor(private val apiService: IntroServices) :
  BaseRemoteDataSource() {

  suspend fun intro() = safeApiCall {
    apiService.intro()
  }
}