package com.structure.base_mvvm.data.auth.data_source.remote

import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.auth.entity.request.ForgetPasswordRequest
import com.structure.base_mvvm.domain.auth.entity.request.LogInRequest
import com.structure.base_mvvm.domain.auth.entity.request.VerifyAccountRequest
import com.structure.base_mvvm.domain.utils.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthServices {

  @POST("v1/client/login")
  suspend fun logIn(@Body request: LogInRequest): BaseResponse<User>

  @POST("v1/auth/send_code")
  suspend fun forgetPassword(@Body request: ForgetPasswordRequest): BaseResponse<*>

  @POST("v1/auth/verify_code")
  suspend fun verifyAccount(@Body request: VerifyAccountRequest): BaseResponse<User>

  @Multipart
  @POST("v1/auth/register")
  suspend fun register(
    @QueryMap map: Map<String, String>,
    @Part image: MultipartBody.Part?
  ): BaseResponse<*>

}