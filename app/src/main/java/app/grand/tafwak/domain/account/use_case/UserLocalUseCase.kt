package app.grand.tafwak.domain.account.use_case

import app.grand.tafwak.domain.account.repository.AccountRepository
import app.grand.tafwak.domain.auth.entity.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalUseCase @Inject constructor(private val accountRepository: AccountRepository) {
  suspend operator fun invoke(user: User) = accountRepository.saveUserToLocal(user)
  suspend operator fun invoke(): Flow<User> = accountRepository.getUserToLocal()
  suspend fun saveUserToken(value: String) =
    accountRepository.saveUserToken(value)

  suspend fun registerStep(value: String) =
    accountRepository.saveRegisterStep(value)

  suspend fun getRegisterStep(): Flow<String> =
    accountRepository.getRegisterStep()

  suspend fun saveCountryId(value: String) =
    accountRepository.saveCountryId(value)

  operator fun invoke(key: String): String {
    return accountRepository.getKeyFromLocal(key)
  }

  suspend fun saveToken(value: String) {
    accountRepository.saveFirebaseTokenToLocal(value)
  }

  suspend fun getToken(): Flow<String> = accountRepository.getFirebaseTokenToLocal()

}