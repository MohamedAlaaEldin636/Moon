package grand.app.moon.data.map.data_source

import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapServices {
  @GET("v1/map")
  suspend fun mapStore(
    @Query("type") type: String,
    @Query("category_id") categoryId: Int?,
    @Query("sub_category_id") subCategoryId: Int?,
    @Query("property_id") propertyId: Int?
  ): BaseResponse<List<Store>>


  @GET("v1/map")
  suspend fun mapAds(
    @Query("type") type: String,
    @Query("property_id") propertyId: String?,
    @Query("sub_category_id") subCategoryId: String?,
    @Query("category_id") categoryId: String?
  ): BaseResponse<List<Advertisement>>
}