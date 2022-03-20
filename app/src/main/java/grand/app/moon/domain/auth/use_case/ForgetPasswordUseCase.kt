package grand.app.moon.domain.auth.use_case

import grand.app.moon.domain.auth.entity.request.ForgetPasswordRequest
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ForgetPasswordUseCase @Inject constructor(
  private val authRepository: AuthRepository
) {
  operator fun invoke(request: ForgetPasswordRequest): Flow<Resource<BaseResponse<*>>> = flow {
    if (request.email.isNotEmpty()) {
      emit(Resource.Loading)
      val result = authRepository.forgetPassword(request)
      emit(result)
    }
  }.flowOn(Dispatchers.IO)
}