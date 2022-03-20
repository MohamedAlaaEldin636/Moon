package grand.app.moon.domain.account.use_case

import grand.app.moon.domain.account.entity.request.SendFirebaseTokenRequest
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
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