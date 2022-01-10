package com.structure.base_mvvm.domain.auth.use_case

import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.VerifyAccountRequest
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class VerifyAccountUseCase @Inject constructor(
  private val authRepository: AuthRepository
) {

  operator fun invoke(request: VerifyAccountRequest): Flow<Resource<BaseResponse<User>>> = flow {
    if (request.code.isNotEmpty()) {
      emit(Resource.Loading)
      val result = authRepository.verifyAccount(request)
      emit(result)
    }
  }.flowOn(Dispatchers.IO)
}