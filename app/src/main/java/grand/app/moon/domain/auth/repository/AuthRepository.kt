package grand.app.moon.domain.auth.repository

import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.helpers.login.SocialRequest

interface AuthRepository {

  suspend fun logIn(request: LogInRequest): Resource<BaseResponse<User>>
  suspend fun socialRegister(request: SocialRequest): Resource<BaseResponse<User>>
  suspend fun logout(): Resource<BaseResponse<*>>
  suspend fun updateProfile(name: String, user: User): Resource<BaseResponse<User?>>
  suspend fun updateProfile(request: UpdateProfileRequest): Resource<BaseResponse<User>>
  suspend fun verifyAccount(request: VerifyAccountRequest): Resource<BaseResponse<User>>
  suspend fun sendCode(request: VerifyAccountRequest): Resource<BaseResponse<*>>
}