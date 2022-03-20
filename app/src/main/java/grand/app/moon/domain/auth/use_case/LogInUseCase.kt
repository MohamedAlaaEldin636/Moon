package grand.app.moon.domain.auth.use_case

import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.*
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
  operator fun invoke(
    request: LogInRequest
  ): Flow<Resource<BaseResponse<User>>> = flow {

    if (checkValidation(request)) {
      emit(Resource.Loading)
      val result = authRepository.logIn(request)
      if (result is Resource.Success) {
        userLocalUseCase.saveUserToken(result.value.data.token)
        userLocalUseCase.saveCountryId(result.value.data.country_id)
        if (result.value.data.register_steps == 4)
          userLocalUseCase.invoke(result.value.data)
        else
          userLocalUseCase.registerStep(
            result.value.data.register_steps.toString()
          )
      }
      emit(result)
    }
  }.flowOn(Dispatchers.IO)

  private  fun checkValidation(request: LogInRequest): Boolean {
    var isValid = true
    if (request.email.isEmpty()) {
      request.validation.emailError.set(Constants.EMPTY)
      isValid = false
    }
    if (!request.email.isValidEmail()) {
      request.validation.emailError.set(Constants.INVALID_EMAIL)
      isValid = false
    }
    return isValid
  }
}