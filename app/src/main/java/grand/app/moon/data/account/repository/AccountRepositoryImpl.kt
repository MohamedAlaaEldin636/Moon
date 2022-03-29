package grand.app.moon.data.account.repository

import grand.app.moon.data.account.data_source.remote.AccountRemoteDataSource
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.account.entity.request.SendFirebaseTokenRequest
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Country
import grand.app.moon.domain.utils.BaseResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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

  override fun getIsLoggedIn(): Boolean {
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

  override fun saveUserToLocal(user: User) {
    appPreferences.saveUser(user)
  }

  override fun getUserToLocal(): User {
    return appPreferences.getUser()
  }

  override suspend fun saveUserToken(userToken: String) {
    appPreferences.userToken(userToken)
  }

  override suspend fun getUserToken(): Flow<String> {
    return appPreferences.getUserToken()
  }

  override suspend fun saveCategories(countries: BaseResponse<ArrayList<CategoryItem>>) {
    appPreferences.saveCategories(countries)
  }

  override suspend fun getCategories(): Flow<BaseResponse<ArrayList<CategoryItem>>> {
    return flowOf(appPreferences.getCategories())
  }


  override suspend fun saveCountries(countries: BaseResponse<List<Country>>) {
    appPreferences.saveCountries(countries)
  }

  override suspend fun getCountries(): Flow<BaseResponse<List<Country>>> {
    return flowOf(appPreferences.getCountries())
  }


  override suspend fun saveCountryId(country_id: String) {
    appPreferences.countryId(country_id)
  }

  override  fun clearUser() {
    appPreferences.clearUser()
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