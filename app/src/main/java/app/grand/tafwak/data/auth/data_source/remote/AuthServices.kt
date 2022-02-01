package app.grand.tafwak.data.auth.data_source.remote

import app.grand.tafwak.domain.auth.entity.model.User
import app.grand.tafwak.domain.auth.entity.request.ChangePasswordRequest
import app.grand.tafwak.domain.auth.entity.request.ForgetPasswordRequest
import app.grand.tafwak.domain.auth.entity.request.LogInRequest
import app.grand.tafwak.domain.auth.entity.request.VerifyAccountRequest
import app.grand.tafwak.domain.utils.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthServices {

  @POST("v1/auth/login")
  suspend fun logIn(@Body request: LogInRequest): BaseResponse<User>

  @POST("v1/auth/send_code")
  suspend fun forgetPassword(@Body request: ForgetPasswordRequest): BaseResponse<*>

  @POST("v1/auth/verify_code")
  suspend fun verifyAccount(@Body request: VerifyAccountRequest): BaseResponse<User>
 @POST("v1/auth/change_password")
  suspend fun changePassword(@Body request: ChangePasswordRequest): BaseResponse<*>

  @Multipart
  @POST("v1/auth/register")
  suspend fun register(
    @QueryMap map: Map<String, String>,
    @Part image: MultipartBody.Part?
  ): BaseResponse<*>

}