package com.structure.base_mvvm.domain.account.repository

import com.structure.base_mvvm.domain.account.entity.request.SendFirebaseTokenRequest
import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

  suspend fun sendFirebaseToken(request: SendFirebaseTokenRequest): Resource<BaseResponse<Boolean>>

  suspend fun logOut(): Resource<BaseResponse<*>>

  suspend fun isLoggedIn(isLoggedIn: Boolean)

  suspend fun getIsLoggedIn(): Flow<Boolean>

  suspend fun saveFirebaseTokenToLocal(firebaseToken: String)

  suspend fun getFirebaseTokenToLocal(): Flow<String>

  suspend fun setFirstTime(isFirstTime: Boolean)

  suspend fun isFirstTime(): Flow<Boolean>

  suspend fun saveUserToLocal(user: User)

  suspend fun getUserToLocal(): Flow<User>

  suspend fun saveUserToken(userToken: String)

  suspend fun getUserToken(): Flow<String>

  suspend fun saveRegisterStep(register_step: String)

  suspend fun getRegisterStep(): Flow<String>

  suspend fun saveCountryId(country_id: String)

  suspend fun getCountryId(): Flow<String>

  fun clearPreferences()

  fun saveKeyToLocal(key: String, value: String)

  fun getKeyFromLocal(key: String): String
}