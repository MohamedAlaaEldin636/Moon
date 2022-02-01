package app.grand.tafwak.data.account.data_source.remote

import app.grand.tafwak.domain.account.entity.request.SendFirebaseTokenRequest
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AccountServices {

  @PUT("Account/FirebaseToken/Update")
  suspend fun sendFirebaseToken(@Body request: SendFirebaseTokenRequest): BaseResponse<Boolean>

  @POST("v1/auth/logout")
  suspend fun logOut(): BaseResponse<*>
}