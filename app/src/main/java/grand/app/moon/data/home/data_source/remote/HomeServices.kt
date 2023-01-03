package grand.app.moon.data.home.data_source.remote

import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.ResponseAnnouncement
import grand.app.moon.domain.home.models.ResponseAppGlobalAnnouncement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeServices {
  @GET("v1/home")
  suspend fun home(): BaseResponse<HomeResponse>

  @GET("v1/under-construction")
  suspend fun getAppGlobalAnnouncement(): BaseResponse<ResponseAppGlobalAnnouncement?>

  @GET("v1/announcement")
  suspend fun getAnnouncement(): BaseResponse<ResponseAnnouncement?>

  @GET("v1/stories")
  suspend fun stories(@Query("category_id") category_id: Int?): BaseResponse<ArrayList<Store>>

  @GET("v1/categories")
  suspend fun getCategories(): BaseResponse<ArrayList<CategoryItem>>

  @GET("v1/categories")
  suspend fun getCategories2(): BaseResponse<List<ItemCategory>?>

  @GET("v1/sub-categories")
  suspend fun getCategoryDetails(@Query("category_id") category_id: Int): BaseResponse<CategoryDetails>

  @GET("v1/check")
  suspend fun getCheckAvailability(@Query("type") type: String): BaseResponse<Int?>


}