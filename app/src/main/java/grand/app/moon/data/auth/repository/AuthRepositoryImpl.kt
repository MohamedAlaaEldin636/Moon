package grand.app.moon.data.auth.repository

import grand.app.moon.data.auth.data_source.remote.AuthRemoteDataSource
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

  override
  suspend fun logIn(request: LogInRequest) = remoteDataSource.logIn(request)

  override suspend fun verifyAccount(request: VerifyAccountRequest): Resource<BaseResponse<User>> =
    remoteDataSource.verifyAccount(request)

  override suspend fun sendCode(request: VerifyAccountRequest): Resource<BaseResponse<*>> =
    remoteDataSource.sendCode(request)

}