package grand.app.moon.data.shop

import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.core.di.module.RetrofitModule
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

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

	@Multipart
	@POST("v1/profile/explores")
	suspend fun addExplore(
		@Part files: List<MultipartBody.Part>,
		@Header(RetrofitModule.HEADER_KEY_TIME_OUT_IN_MINUTES) infiniteTimeout: String = 30.toString(),
	): BaseResponse<Any?>

	@GET("v1/profile/explores")
	suspend fun getExplores(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>,
	): MABaseResponse<ResponseExploreInShopInfo>

	@DELETE("v1/profile/explores/{id}")
	suspend fun deleteExplore(
		@Path("id") id: Int,
	): BaseResponse<Any?>

}
