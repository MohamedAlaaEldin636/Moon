package app.grand.tafwak.data.countries.data_source

import app.grand.tafwak.domain.countries.entity.Country
import app.grand.tafwak.domain.countries.entity.request.RegisterStep2
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.*

interface CountriesServices {

  @GET("v1/countries")
  suspend fun countries(): BaseResponse<List<Country>>
  @POST("v1/auth/register")
  suspend fun registerStep2(@Body request: RegisterStep2): BaseResponse<*>

}