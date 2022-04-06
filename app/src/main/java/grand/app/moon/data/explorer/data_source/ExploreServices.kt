package grand.app.moon.data.explorer.data_source

import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.explorer.entity.ExploreListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET

interface ExploreServices {
  @GET("v1/explores")
  suspend fun explores(): BaseResponse<ExploreListPaginateData>
}