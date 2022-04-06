package grand.app.moon.data.map.data_source

import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.explorer.entity.ExploreListPaginateData
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MapServices {
  @GET("v1/map")
  suspend fun map(@Query("type")type: String): BaseResponse<List<Store>>
}