package app.grand.tafwak.domain.account.use_case

import app.grand.tafwak.domain.account.entity.request.SendFirebaseTokenRequest
import app.grand.tafwak.domain.account.repository.AccountRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SendFirebaseTokenUseCase @Inject constructor(private val accountRepository: AccountRepository) {

  operator fun invoke(
    token: String,
    deviceType: String,
    deviceId: String
  ): Flow<Resource<BaseResponse<Boolean>>> = flow {
    emit(accountRepository.sendFirebaseToken(SendFirebaseTokenRequest(token, deviceType, deviceId)))
  }.flowOn(Dispatchers.IO)
}