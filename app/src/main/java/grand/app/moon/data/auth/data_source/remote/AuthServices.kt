package grand.app.moon.data.auth.data_source.remote

import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.login.SocialRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AuthServices {

  @POST("v1/authentication")
  suspend fun logIn(@Body request: LogInRequest): BaseResponse<User>

  @POST("v1/auth/social/login")
  suspend fun socialRegister(@Body request: SocialRequest): BaseResponse<User>

  @POST("v1/auth/logout")
  suspend fun logout(): BaseResponse<*>

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

  @POST("v1/profile")
  suspend fun updateProfile(@Body request: UpdateProfileRequest): BaseResponse<User>

	@FormUrlEncoded
  @POST("v1/profile")
  suspend fun updateProfile(
		@Field("name") name: RequestBody,
		@Field("country_code") countryCode: RequestBody,
		@Field("phone") phone: RequestBody,
	): BaseResponse<User?>

  @Multipart
  @POST("v1/profile")
  suspend fun updateProfile(
    @PartMap mapOfOtherParams: Map<String,@JvmSuppressWildcards RequestBody>,
    @Part image: MultipartBody.Part?
  ): BaseResponse<User>
}