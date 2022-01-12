package com.structure.base_mvvm.data.educational.data_source

import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.educational.entity.model.Grade
import com.structure.base_mvvm.domain.educational.entity.model.Stage
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep3
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep4
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.*

interface EducationalServices {

  @GET("v1/educational_stages")
  suspend fun educationalStages(): BaseResponse<List<Stage>>

  @GET("v1/grades")
  suspend fun educationalStageGrade(@Query("educational_stage_id") stage_id: Int): BaseResponse<List<Grade>>

  @POST("v1/auth/register")
  suspend fun registerStep3(@Body request: RegisterStep3): BaseResponse<*>

  @POST("v1/auth/register")
  suspend fun registerStep4(@Body request: RegisterStep4): BaseResponse<User>

}