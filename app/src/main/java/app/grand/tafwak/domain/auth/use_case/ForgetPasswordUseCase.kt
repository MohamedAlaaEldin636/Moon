package app.grand.tafwak.domain.auth.use_case

import app.grand.tafwak.domain.auth.entity.request.ForgetPasswordRequest
import app.grand.tafwak.domain.auth.repository.AuthRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
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