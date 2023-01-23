package grand.app.moon.data.shop

import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

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

	@Multipart
	@POST("v1/profile/working-hours")
	suspend fun saveWorkingHours(
		@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>
	): BaseResponse<Any?>

	@GET("v1/profile/social-media")
	suspend fun getSocialMedia(): BaseResponse<List<ResponseStoreSocialMedia>?>

	@Multipart
	@POST("v1/profile/social-media")
	suspend fun saveSocialMedia(
		@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>
	): BaseResponse<Any?>

	@GET("v1/profile/reviews")
	suspend fun getClientsReviews(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>,
	): MABaseResponse<MABasePaging<ResponseClientReviews>>

}
