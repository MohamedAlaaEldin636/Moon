package com.structure.base_mvvm.domain.auth.use_case

import com.structure.base_mvvm.domain.auth.entity.request.ForgetPasswordRequest
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
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