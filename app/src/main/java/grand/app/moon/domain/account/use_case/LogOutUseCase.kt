package grand.app.moon.domain.account.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val accountRepository: AccountRepository) {

  operator fun invoke(): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)

    val result = accountRepository.logOut()
    if (result is Resource.Success) {
      accountRepository.clearPreferences()
    }

    emit(result)
  }.flowOn(Dispatchers.IO)
}