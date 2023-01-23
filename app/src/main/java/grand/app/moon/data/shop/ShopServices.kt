package grand.app.moon.data.shop

import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.domain.shop.ResponseStoreSubCategory
import grand.app.moon.domain.shop.ResponseWorkingHour
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopServices {

	@GET("v1/my-categories")
	suspend fun getMyCategories(
		@Query("page") page: Int,
	): MABaseResponse<MABasePaging<IdAndName>>

	@DELETE("v1/my-categories/{id}")
	suspend fun deleteCategory(
		@Path("id") id: Int,
	): BaseResponse<Any?>

	@FormUrlEncoded
	@POST("v1/my-categories")
	suspend fun createCategory(
		@Field("name") name: String,
	): BaseResponse<Any?>

	@FormUrlEncoded
	@POST("v1/my-categories/update/{id}")
	suspend fun updateCategory(
		@Path("id") id: Int,
		@Field("name") name: String,
	): BaseResponse<Any?>

	@GET("v1/my-categories/subs")
	suspend fun getMySubCategories(
		@Query("page") page: Int,
	): MABaseResponse<MABasePaging<ResponseStoreSubCategory>>

	@DELETE("v1/my-categories/{id}")
	suspend fun deleteSubCategory(
		@Path("id") id: Int,
	): BaseResponse<Any?>

	@FormUrlEncoded
	@POST("v1/my-categories")
	suspend fun createSubCategory(
		@Field("name") name: String,
		@Field("parent_id") parentId: Int,
	): BaseResponse<Any?>

	@FormUrlEncoded
	@POST("v1/my-categories/update/{id}")
	suspend fun updateSubCategory(
		@Path("id") id: Int,
		@Field("name") name: String,
		@Field("parent_id") parentId: Int,
	): BaseResponse<Any?>

	@GET("v1/profile/working-hours")
	suspend fun getWorkingHours(): BaseResponse<List<ResponseWorkingHour>?>

	@FormUrlEncoded
	@POST("v1/profile/working-hours")
	suspend fun saveWorkingHours(
		@FieldMap map: Map<String, String>
	): BaseResponse<Any?>

	@GET("v1/profile/social-media")
	suspend fun getSocialMedia(): BaseResponse<List<ResponseStoreSocialMedia>?>

	@FormUrlEncoded
	@POST("v1/profile/social-media")
	suspend fun saveSocialMedia(
		@FieldMap map: Map<String, String>
	): BaseResponse<Any?>

}
