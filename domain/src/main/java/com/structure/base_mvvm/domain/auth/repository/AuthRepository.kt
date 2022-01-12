package com.structure.base_mvvm.domain.auth.repository

import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.*
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface AuthRepository {

  suspend fun logIn(request: LogInRequest): Resource<BaseResponse<User>>
  suspend fun changePassword(request: ChangePasswordRequest): Resource<BaseResponse<*>>
  suspend fun forgetPassword(request: ForgetPasswordRequest): Resource<BaseResponse<*>>
  suspend fun register(request: RegisterRequest): Resource<BaseResponse<*>>
  suspend fun verifyAccount(request: VerifyAccountRequest): Resource<BaseResponse<User>>
}