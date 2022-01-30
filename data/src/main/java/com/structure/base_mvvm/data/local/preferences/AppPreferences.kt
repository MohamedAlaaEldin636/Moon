package com.structure.base_mvvm.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.structure.base_mvvm.domain.auth.entity.model.User
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(private val context: Context) {

  private val STORE_NAME = "app_data_store"
  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

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

  fun getIsLoggedIn() = context.dataStore.data.map {
    it[IS_LOGGED_IN] ?: false
  }

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

  suspend fun saveUser(user: User) {
    Log.e("saveUser", "saveUser: " + user.account_type)
    context.dataStore.edit { preferences ->
      preferences[USER_NAME] = user.name
      preferences[EMAIL] = user.email
      preferences[USER_PHONE] = user.phone
      preferences[USER_ID] = user.id
      preferences[ACCOUNT_TYPE] = user.account_type
    }
  }

  fun getUser() = context.dataStore.data.map { preferences ->
    User(
      name = preferences[USER_NAME] ?: "",
      email = preferences[EMAIL] ?: "",
      id = preferences[USER_ID] ?: 0,
      phone = preferences[USER_PHONE] ?: "",
      account_type = preferences[ACCOUNT_TYPE] ?: ""
    )
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
  private val sessionPreferences: SharedPreferences =
    context.getSharedPreferences(SESSION_PREFERENCES_NAME, MODE)

  private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
  }

  fun setLocal(key: String, value: String) {
    sessionPreferences.edit {
      it.putString(key, value)
    }
  }

  fun getLocal(key: String): String {
    return sessionPreferences.getString(key, "").toString()
  }

  fun clearPreferences() {
    sessionPreferences.edit {
      it.clear().apply()
    }
  }
}