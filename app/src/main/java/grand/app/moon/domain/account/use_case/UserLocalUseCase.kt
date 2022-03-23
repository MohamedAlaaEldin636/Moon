package grand.app.moon.domain.account.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.auth.entity.model.User
//import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.utils.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalUseCase @Inject constructor(private val accountRepository: AccountRepository) {
  suspend operator fun invoke(user: User) = accountRepository.saveUserToLocal(user)
  suspend operator fun invoke(): Flow<User> = accountRepository.getUserToLocal()
  suspend fun saveUserToken(value: String) =
    accountRepository.saveUserToken(value)

//  suspend fun getCountries(): Flow<BaseResponse<List<Country>>> =
//    accountRepository.getCountries()
//
//  suspend fun saveCountries(countries: BaseResponse<List<Country>>) =
//    accountRepository.saveCountries(countries)

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