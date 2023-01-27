package grand.app.moon.data.auth.data_source.remote

import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.helpers.login.SocialRequest
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val apiService: AuthServices) :
  BaseRemoteDataSource() {

  suspend fun logIn(request: LogInRequest) = safeApiCall {
    apiService.logIn(request)
  }

  suspend fun socialRegister(request: SocialRequest) = safeApiCall {
    apiService.socialRegister(request)
  }

  suspend fun logout() = safeApiCall {
    apiService.logout()
  }

  suspend fun logoutSuspend() = safeApiCall {
    apiService.logoutSuspend()
  }

  suspend fun verifyAccount(request: VerifyAccountRequest) = safeApiCall {
    apiService.verifyAccount(request)
  }

  suspend fun sendCode(request: VerifyAccountRequest) = safeApiCall {
    apiService.sendCode(request)
  }

  suspend fun updateProfile(name: String, user: User) = safeApiCall {
    apiService.updateProfile(name, user.country_code, user.phone, "Bearer ${user.token}")
  }

  suspend fun updateProfile(request: UpdateProfileRequest) = safeApiCall {


    val map = mutableMapOf(
      "name" to request.name.toRequestBody(),
      "email" to request.email.toRequestBody(),
      "phone" to request.phone.toRequestBody(),
      "country_code" to request.country_code.toRequestBody(),
    )


//    apiService.updateProfile(getParameters(request), request.image[0])
    when{
      request.uri == null -> apiService.updateProfile(request)
      else ->{
        apiService.updateProfile(
          map,
          request.uri?.createMultipartBodyPart(MyApplication.instance, "image")
        )
      }
    }
  }
}