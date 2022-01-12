package com.structure.base_mvvm.data.account.repository

import com.structure.base_mvvm.data.account.data_source.remote.AccountRemoteDataSource
import com.structure.base_mvvm.data.local.preferences.AppPreferences
import com.structure.base_mvvm.domain.account.entity.request.SendFirebaseTokenRequest
import com.structure.base_mvvm.domain.account.repository.AccountRepository
import com.structure.base_mvvm.domain.auth.entity.model.User
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

  override
  fun isFirstTime() = appPreferences.isFirstTime

  override
  fun isLoggedIn() = appPreferences.isLoggedIn

  override suspend fun saveFirebaseTokenToLocal(firebaseToken: String) {
//    appPreferences.firebaseToken = firebaseToken
    appPreferences.saveNameToDataStore(firebaseToken)
  }

  override suspend fun getFirebaseTokenToLocal(): Flow<String> {
    return appPreferences.getNameFromDataStore()
  }

  override
  fun getFirebaseToken() = appPreferences.firebaseToken

  override
  fun saveUserToLocal(user: User) {
    appPreferences.userData = user
  }

  override fun getUserToLocal(): User? {
    return appPreferences.userData
  }

  override
  fun setFirstTime(isFirstTime: Boolean) {
    appPreferences.isFirstTime = isFirstTime
  }

  override
  fun clearPreferences() = appPreferences.clearPreferences()
  override fun saveKeyToLocal(key: String, value: String) {
    appPreferences.setLocal(key, value)
  }

  override fun getKeyFromLocal(key: String): String = appPreferences.getLocal(key)
}