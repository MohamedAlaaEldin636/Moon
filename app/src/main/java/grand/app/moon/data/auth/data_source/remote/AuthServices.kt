package grand.app.moon.data.auth.data_source.remote

import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.utils.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthServices {

  @POST("v1/authentication")
  suspend fun logIn(@Body request: LogInRequest): BaseResponse<*>

  @POST("v1/auth/verify_code")
  suspend fun verifyAccount(@Body request: VerifyAccountRequest): BaseResponse<User>

  @POST("v1/auth/send_code")
  suspend fun sendCode(@Body request: VerifyAccountRequest): BaseResponse<User>

  @Multipart
  @POST("v1/auth/register")
  suspend fun register(
    @QueryMap map: Map<String, String>,
    @Part image: MultipartBody.Part?
  ): BaseResponse<*>

}