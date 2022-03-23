package grand.app.moon.data.country.data_source

import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET

interface CountriesServices {
  @GET("v1/countries")
  suspend fun countries(): BaseResponse<List<Country>>
}