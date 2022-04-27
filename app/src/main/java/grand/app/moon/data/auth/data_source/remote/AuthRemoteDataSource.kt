package grand.app.moon.data.auth.data_source.remote

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.helpers.login.SocialRequest
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val apiService: AuthServices) :
  BaseRemoteDataSource() {

  suspend fun logIn(request: VerifyAccountRequest) = safeApiCall {
    apiService.logIn(request)
  }

  suspend fun socialRegister(request: SocialRequest) = safeApiCall {
    apiService.socialRegister(request)
  }

  suspend fun verifyAccount(request: VerifyAccountRequest) = safeApiCall {
    apiService.verifyAccount(request)
  }

  suspend fun sendCode(request: VerifyAccountRequest) = safeApiCall {
    apiService.sendCode(request)
  }

  suspend fun updateProfile(request: UpdateProfileRequest) = safeApiCall {
    if (request.image.isEmpty())
      apiService.updateProfile(request)
    else
      apiService.updateProfile(getParameters(request), request.image[0])
  }
}