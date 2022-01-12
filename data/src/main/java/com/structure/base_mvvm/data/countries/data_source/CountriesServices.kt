package com.structure.base_mvvm.data.countries.data_source

import com.structure.base_mvvm.domain.countries.entity.Country
import com.structure.base_mvvm.domain.countries.entity.request.RegisterStep2
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.*

interface CountriesServices {

  @GET("v1/countries")
  suspend fun countries(): BaseResponse<List<Country>>
  @POST("v1/auth/register")
  suspend fun registerStep2(@Body request: RegisterStep2): BaseResponse<*>

}