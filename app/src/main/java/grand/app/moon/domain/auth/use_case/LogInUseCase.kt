package grand.app.moon.domain.auth.use_case

import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.*
import grand.app.moon.helpers.login.SocialRequest
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class LogInUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userLocalUseCase: UserLocalUseCase
) {
  lateinit var baseViewModel: BaseViewModel

  operator fun invoke(
    request: LogInRequest
  ): Flow<Resource<BaseResponse<*>>> = flow {

    emit(Resource.Loading)
    val result = authRepository.logIn(request)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun updateProfile(
    request: UpdateProfileRequest
  ): Flow<Resource<BaseResponse<User>>> = flow {

    emit(Resource.Loading)
    val result = authRepository.updateProfile(request)
    if(result is Resource.Success){
      userLocalUseCase(
        result.value.data
      )
    }
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun socialRegister(
    request: SocialRequest
  ): Flow<Resource<BaseResponse<User>>> = flow {

    emit(Resource.Loading)
    val result = authRepository.socialRegister(request)
    if(result is Resource.Success){
      userLocalUseCase(
        result.value.data
      )
    }
    emit(result)
  }.flowOn(Dispatchers.IO)

}