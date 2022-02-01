package app.grand.tafwak.data.account.data_source.remote

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import app.grand.tafwak.domain.account.entity.request.SendFirebaseTokenRequest
import javax.inject.Inject

class AccountRemoteDataSource @Inject constructor(private val apiService: AccountServices) :
  BaseRemoteDataSource() {

  suspend fun sendFirebaseToken(request: SendFirebaseTokenRequest) = safeApiCall {
    apiService.sendFirebaseToken(request)
  }

  suspend fun logOut() = safeApiCall {
    apiService.logOut()
  }
}