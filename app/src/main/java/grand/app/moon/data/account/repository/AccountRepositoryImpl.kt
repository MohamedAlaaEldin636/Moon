package grand.app.moon.data.account.repository

import grand.app.moon.data.account.data_source.remote.AccountRemoteDataSource
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.account.entity.request.SendFirebaseTokenRequest
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.auth.entity.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
  private val remoteDataSource: AccountRemoteDataSource,
  private val appPreferences: AppPreferences
) : AccountRepository {

  override
  suspend fun sendFirebaseToken(request: SendFirebaseTokenRequest) =
    remoteDataSource.sendFirebaseToken(request)

  override
  suspend fun logOut() = remoteDataSource.logOut()
  override suspend fun isLoggedIn(isLoggedIn: Boolean) {
    appPreferences.isLoggedIn(isLoggedIn)
  }

  override suspend fun getIsLoggedIn(): Flow<Boolean> {
    return appPreferences.getIsLoggedIn()
  }


  override suspend fun saveFirebaseTokenToLocal(firebaseToken: String) {
    appPreferences.saveFireBaseToken(firebaseToken)
  }

  override suspend fun getFirebaseTokenToLocal(): Flow<String> {
    return appPreferences.getFireBaseToken()
  }

  override suspend fun setFirstTime(isFirstTime: Boolean) {
    appPreferences.isFirstTime(isFirstTime)
  }

  override suspend fun isFirstTime(): Flow<Boolean> {
    return appPreferences.getIsFirstTime()
  }

  override suspend fun saveUserToLocal(user: User) {
    appPreferences.saveUser(user)
  }

  override suspend fun getUserToLocal(): Flow<User> {
    return appPreferences.getUser()
  }

  override suspend fun saveUserToken(userToken: String) {
    appPreferences.userToken(userToken)
  }

  override suspend fun getUserToken(): Flow<String> {
    return appPreferences.getUserToken()
  }

  override suspend fun saveRegisterStep(register_step: String) {
    appPreferences.registerStep(register_step)
  }

  override suspend fun getRegisterStep(): Flow<String> {
    return appPreferences.getRegisterStep()
  }

  override suspend fun saveCountryId(country_id: String) {
    appPreferences.countryId(country_id)
  }

  override suspend fun getCountryId(): Flow<String> {
    return appPreferences.getCountryId()
  }

  override
  fun clearPreferences() = appPreferences.clearPreferences()
  override fun saveKeyToLocal(key: String, value: String) {
    appPreferences.setLocal(key, value)
  }

  override fun getKeyFromLocal(key: String): String = appPreferences.getLocal(key)
}