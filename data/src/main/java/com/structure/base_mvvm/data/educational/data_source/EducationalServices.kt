package com.structure.base_mvvm.data.educational.data_source

import com.structure.base_mvvm.domain.educational.entity.Stage
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.*

interface EducationalServices {

  @GET("v1/educational_stages")
  suspend fun educationalStages(): BaseResponse<List<Stage>>

}