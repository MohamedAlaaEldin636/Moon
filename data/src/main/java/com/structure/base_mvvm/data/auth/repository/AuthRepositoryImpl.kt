package com.structure.base_mvvm.data.auth.repository

import com.structure.base_mvvm.data.auth.data_source.remote.AuthRemoteDataSource
import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.*
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

  override
  suspend fun logIn(request: LogInRequest) = remoteDataSource.logIn(request)
  override suspend fun changePassword(request: ChangePasswordRequest): Resource<BaseResponse<*>> =
    remoteDataSource.changePassword(request)

  override suspend fun forgetPassword(request: ForgetPasswordRequest) =
    remoteDataSource.forgetPassword(request)

  override suspend fun register(request: RegisterRequest): Resource<BaseResponse<*>> =
    remoteDataSource.register(request)

  override suspend fun verifyAccount(request: VerifyAccountRequest): Resource<BaseResponse<User>> =
    remoteDataSource.verifyAccount(request)

}