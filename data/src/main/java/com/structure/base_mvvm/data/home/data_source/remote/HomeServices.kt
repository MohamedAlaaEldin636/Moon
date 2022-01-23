package com.structure.base_mvvm.data.home.data_source.remote

import com.structure.base_mvvm.domain.home.models.HomeStudentData
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.GET

interface HomeServices {
  @GET("v1/student/home")
  suspend fun homeStudent(): BaseResponse<HomeStudentData>

}