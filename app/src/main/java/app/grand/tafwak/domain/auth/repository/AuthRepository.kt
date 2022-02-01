package app.grand.tafwak.domain.auth.repository

import app.grand.tafwak.domain.auth.entity.model.User
import app.grand.tafwak.domain.auth.entity.request.*
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface AuthRepository {

  suspend fun logIn(request: LogInRequest): Resource<BaseResponse<User>>
  suspend fun changePassword(request: ChangePasswordRequest): Resource<BaseResponse<*>>
  suspend fun forgetPassword(request: ForgetPasswordRequest): Resource<BaseResponse<*>>
  suspend fun register(request: RegisterRequest): Resource<BaseResponse<*>>
  suspend fun verifyAccount(request: VerifyAccountRequest): Resource<BaseResponse<User>>
}