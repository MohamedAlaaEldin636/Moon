package grand.app.moon.domain.auth.use_case

import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class VerifyAccountUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userLocalUseCase: UserLocalUseCase
) {

  operator fun invoke(request: VerifyAccountRequest): Flow<Resource<BaseResponse<User>>> = flow {
    if (request.code.isNotEmpty()) {
      emit(Resource.Loading)
      val result = authRepository.verifyAccount(request)
      if (result is Resource.Success) {
        userLocalUseCase(
          result.value.data!!
        )
      }
      emit(result)
    }
  }.flowOn(Dispatchers.IO)

  fun sendCode(request: VerifyAccountRequest): Flow<Resource<BaseResponse<*>>> = flow {
    if (request.code.isNotEmpty()) {
      emit(Resource.Loading)
      val result = authRepository.sendCode(request)
      emit(result)
    }
  }.flowOn(Dispatchers.IO)

}