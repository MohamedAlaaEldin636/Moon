package grand.app.moon.domain.account.repository

import grand.app.moon.domain.account.entity.request.SendFirebaseTokenRequest
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

  suspend fun sendFirebaseToken(request: SendFirebaseTokenRequest): Resource<BaseResponse<Boolean>>

  suspend fun logOut(): Resource<BaseResponse<*>>

  suspend fun isLoggedIn(isLoggedIn: Boolean)

  fun getIsLoggedIn(): Boolean

  suspend fun saveFirebaseTokenToLocal(firebaseToken: String)

  suspend fun getFirebaseTokenToLocal(): Flow<String>

  suspend fun setFirstTime(isFirstTime: Boolean)

  suspend fun isFirstTime(): Flow<Boolean>

  fun saveUserToLocal(user: User)

  fun getUserToLocal(): User

  suspend fun saveUserToken(userToken: String)

  suspend fun getUserToken(): Flow<String>

  suspend fun saveCategories(countries: BaseResponse<ArrayList<CategoryItem>>)

  suspend fun getCategories(): Flow<BaseResponse<ArrayList<CategoryItem>>>

  suspend fun saveCountries(countries: BaseResponse<List<Country>>)

  suspend fun getCountries(): Flow<BaseResponse<List<Country>>>

  suspend fun saveCountryId(country_id: String)

  suspend fun getCountryId(): Flow<String>

  fun clearPreferences()

  fun saveKeyToLocal(key: String, value: String)
  fun clearUser()

  fun saveSearch(search: String)
  fun getSearches() : ArrayList<String?>?


  fun getKeyFromLocal(key: String): String
}