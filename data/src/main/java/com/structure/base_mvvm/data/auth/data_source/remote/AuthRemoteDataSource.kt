package com.structure.base_mvvm.data.auth.data_source.remote

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import com.structure.base_mvvm.domain.auth.entity.request.ForgetPasswordRequest
import com.structure.base_mvvm.domain.auth.entity.request.LogInRequest
import com.structure.base_mvvm.domain.auth.entity.request.RegisterRequest
import com.structure.base_mvvm.domain.auth.entity.request.VerifyAccountRequest
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val apiService: AuthServices) :
  BaseRemoteDataSource() {

  suspend fun logIn(request: LogInRequest) = safeApiCall {
    apiService.logIn(request)
  }

  suspend fun forgetPassword(request: ForgetPasswordRequest) = safeApiCall {
    apiService.forgetPassword(request)
  }

  suspend fun verifyAccount(request: VerifyAccountRequest) = safeApiCall {
    apiService.verifyAccount(request)
  }

  suspend fun register(request: RegisterRequest) = safeApiCall {
    apiService.register(getParameters(request), request.image[0])
  }

}