package app.grand.tafwak.data.auth.repository

import app.grand.tafwak.data.auth.data_source.remote.AuthRemoteDataSource
import app.grand.tafwak.domain.auth.entity.model.User
import app.grand.tafwak.domain.auth.entity.request.*
import app.grand.tafwak.domain.auth.repository.AuthRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
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