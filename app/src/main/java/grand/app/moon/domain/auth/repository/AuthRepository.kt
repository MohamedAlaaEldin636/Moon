package grand.app.moon.domain.auth.repository

import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface AuthRepository {

  suspend fun logIn(request: LogInRequest): Resource<BaseResponse<*>>
  suspend fun updateProfile(request: UpdateProfileRequest): Resource<BaseResponse<User>>
  suspend fun verifyAccount(request: VerifyAccountRequest): Resource<BaseResponse<User>>
  suspend fun sendCode(request: VerifyAccountRequest): Resource<BaseResponse<*>>
}