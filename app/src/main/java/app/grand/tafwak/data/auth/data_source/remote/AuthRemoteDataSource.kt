package app.grand.tafwak.data.auth.data_source.remote

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import app.grand.tafwak.domain.auth.entity.request.*
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

  suspend fun changePassword(request: ChangePasswordRequest) = safeApiCall {
    apiService.changePassword(request)
  }

  suspend fun register(request: RegisterRequest) = safeApiCall {
    apiService.register(getParameters(request), request.image[0])
  }

}