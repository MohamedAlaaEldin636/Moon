package grand.app.moon.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(private val context: Context) {

  private val STORE_NAME = "app_data_store"
  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

  private val USER_DATA = Pair("USER_DATA", "")


  suspend fun saveFireBaseToken(token: String) {
    context.dataStore.edit {
      it[FIREBASE_TOKEN] = token
    }
  }

  fun getFireBaseToken() = context.dataStore.data.map {
    it[FIREBASE_TOKEN] ?: ""
  }

  suspend fun isFirstTime(isFirstTime: Boolean) {
    context.dataStore.edit {
      it[FIRST_TIME] = isFirstTime
    }
  }

  fun getIsFirstTime() = context.dataStore.data.map {
    it[FIRST_TIME] ?: true
  }

  suspend fun isLoggedIn(isLoggedIn: Boolean) {
    context.dataStore.edit {
      it[IS_LOGGED_IN] = isLoggedIn
    }
  }

//  fun getIsLoggedIn() = context.dataStore.data.map {
//    it[IS_LOGGED_IN] ?: false
//  }

  suspend fun userToken(userToken: String) {
    context.dataStore.edit {
      it[USER_TOKEN] = userToken
    }
  }

  fun getUserToken() = context.dataStore.data.map {
    it[USER_TOKEN] ?: ""
  }

  suspend fun registerStep(register_step: String) {
    context.dataStore.edit {
      it[REGISTER_STEP] = register_step
    }
  }

  fun getRegisterStep() = context.dataStore.data.map {
    it[REGISTER_STEP] ?: ""
  }

  suspend fun countryId(country_id: String) {
    context.dataStore.edit {
      it[COUNTRY_ID] = country_id
    }
  }

  fun getCountryId() = context.dataStore.data.map {
    it[COUNTRY_ID] ?: "1"
  }

  //Old Pref
  companion object {
    val FIREBASE_TOKEN = stringPreferencesKey("FIREBASE_TOKEN")
    val USER_TOKEN = stringPreferencesKey("USER_TOKEN")
    val REGISTER_STEP = stringPreferencesKey("REGISTER_STEP")
    val COUNTRY_ID = stringPreferencesKey("COUNTRY_ID")
    val USER_NAME = stringPreferencesKey("USER_NAME")
    val EMAIL = stringPreferencesKey("EMAIL")
    val USER_ID = intPreferencesKey("USER_ID")
    val USER_PHONE = stringPreferencesKey("USER_PHONE")
    val ACCOUNT_TYPE = stringPreferencesKey("ACCOUNT_TYPE")
    val FIRST_TIME = booleanPreferencesKey("FIRST_TIME")
    val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
    const val APP_PREFERENCES_NAME = "APP-NAME-Cache"
    private const val SESSION_PREFERENCES_NAME = "APP-NAME-UserCache"
    private const val MODE = Context.MODE_PRIVATE
  }

  private val appPreferences: SharedPreferences =
    context.getSharedPreferences(APP_PREFERENCES_NAME, MODE)

  private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
  }

  fun getIsLoggedIn(): Boolean {
    return appPreferences.getInt("id", -1) != -1
  }

  fun saveUser(user: User) {
    appPreferences.edit {
      it.putString(USER_DATA.first, Gson().toJson(user))
      it.putString(Constants.TOKEN, user.token)

      it.putInt("id", user.id)
      it.apply()
    }
  }

  private  val TAG = "AppPreferences"
  fun getUser(): User {
    val value: String? = appPreferences.getString(USER_DATA.first, USER_DATA.second)
    Log.d(TAG, "getUser: $value")
    if (value != null && value.isNotEmpty())
      return Gson().fromJson(value, User::class.java)
    else return User()
  }


  fun clearUser() {
    appPreferences.edit {
      it.putString(USER_DATA.first, "")
      it.putString(Constants.TOKEN, "")
      it.putInt("id", -1)
      it.apply()
    }
  }

  fun setLocal(key: String, value: String) {
    appPreferences.edit {
      it.putString(key, value)
    }
  }

  fun saveSearch(key: String) {
    val arrayList = getSearches()
    arrayList?.add(key)
    val gson = Gson()
    val json =
      gson.toJson(arrayList, object : TypeToken<ArrayList<String>>() {}.type)
    appPreferences.edit {
      it.putString("searches", json)
    }
  }

  fun getSearches() : ArrayList<String?>? {
    val gson = Gson()
    val searches = appPreferences.getString("searches", "")
    return gson.fromJson(
      searches,
      object : TypeToken<ArrayList<String>>() {}.type
    )
  }

  fun getLocal(key: String): String {
    Log.d(TAG, "getLocal: ${appPreferences.getString(key, "")}")
    return appPreferences.getString(key, "").toString()
  }

  fun clearPreferences() {
    appPreferences.edit {
      it.clear().apply()
    }
  }


  fun saveCategories(response: BaseResponse<ArrayList<CategoryItem>>) {
    val gson = Gson()
    val json =
      gson.toJson(response, object : TypeToken<BaseResponse<ArrayList<CategoryItem>>>() {}.type)
    appPreferences.edit {
      it.putString("categoriesResponse", json)
    }
  }

  fun getCategories(): BaseResponse<ArrayList<CategoryItem>> {
    val gson = Gson()
    val countriesResponse = appPreferences.getString("categoriesResponse", "")
    return gson.fromJson(
      countriesResponse,
      object : TypeToken<BaseResponse<ArrayList<CategoryItem>>>() {}.type
    )
  }

  fun saveCountries(countriesResponse: BaseResponse<List<Country>>) {
    val gson = Gson()
    val json =
      gson.toJson(countriesResponse, object : TypeToken<BaseResponse<List<Country>>>() {}.type)
    appPreferences.edit {
      it.putString("countriesResponse", json)
    }
  }

  fun getCountries(): BaseResponse<List<Country>> {
    val gson = Gson()
    val countriesResponse = appPreferences.getString("countriesResponse", "")
    return gson.fromJson(
      countriesResponse,
      object : TypeToken<BaseResponse<List<Country>>>() {}.type
    )
  }
}
