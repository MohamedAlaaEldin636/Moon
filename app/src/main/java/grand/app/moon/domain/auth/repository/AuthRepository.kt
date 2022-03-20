package grand.app.moon.domain.auth.repository

import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface AuthRepository {

  suspend fun logIn(request: LogInRequest): Resource<BaseResponse<User>>
  suspend fun changePassword(request: ChangePasswordRequest): Resource<BaseResponse<*>>
  suspend fun forgetPassword(request: ForgetPasswordRequest): Resource<BaseResponse<*>>
  suspend fun register(request: RegisterRequest): Resource<BaseResponse<*>>
  suspend fun verifyAccount(request: VerifyAccountRequest): Resource<BaseResponse<User>>
}