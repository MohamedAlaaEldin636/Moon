package grand.app.moon.data.shop

import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.core.di.module.RetrofitModule
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.presentation.stats.models.ResponseGeneralStats
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

	@GET("v1/my-categories/subs")
	suspend fun getMySubCategoriesWithParentId(
		@Query("parent_id") parentId: Int,
		@Query("page") page: Int,
	): MABaseResponse<MABasePaging<IdAndName>>

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

	@GET("v1/profile/reviews")
	suspend fun getShopClientsReviews(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>,
	): MABaseResponse<ResponseReviewsWithStats>

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

	@Multipart
	@POST("v1/profile/stories")
	suspend fun addStory(
		@Part file: List<MultipartBody.Part>,
		@Part("story_link_type") storyLinkType: Int,
		@Part("highlight") storyType: Int,
		@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
		@Header(RetrofitModule.HEADER_KEY_TIME_OUT_IN_MINUTES) infiniteTimeout: String = 30.toString(),
	): BaseResponse<Any?>

	@GET("v1/profile/stories")
	suspend fun getStories(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>,
	): MABaseResponse<ResponseStoriesInShopInfo>

	@DELETE("v1/profile/stories/{id}")
	suspend fun deleteStory(
		@Path("id") id: Int,
	): BaseResponse<Any?>

	@GET("v1/reviews")
	suspend fun getClientsReviewsForAdv(
		@Query("advertisement_id") advertisementId: Int,
		@Query("page") page: Int,
	): MABaseResponse<MABasePaging<ResponseClientReviews>>

	@FormUrlEncoded
	@POST("v1/review")
	suspend fun addReviewForAdv(
		@Field("advertisement_id") advertisement_id: Int,
		@Field("review") review: String,
		@FieldMap map: Map<String, String>,
	): BaseResponse<Any?>

	@GET("v1/statistics/show")
	suspend fun getMyAdvStats(
		@Query("advertisement_id") advId: Int,
		@Query("type") type: String,
		@Query("statistics_type") statisticsType: String = "advertisement",
	): BaseResponse<ResponseGeneralStats?>

	@GET("v1/statistics/show")
	suspend fun getMyAdvStatsUsers(
		@Query("advertisement_id") advId: Int,
		@Query("type") type: String,
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>,
		@Query("statistics_type") statisticsType: String = "advertisement",
	): MABaseResponse<ResponseGeneralStats?>

	@GET("v1/reviews")
	suspend fun getReviewsForAdv(
		@Query("advertisement_id") advertisementId: Int,
		@Query("page") page: Int,
	): MABaseResponse<ResponseReviewsWithStats>

	@GET("v1/reviews")
	suspend fun getReviewsForStore(
		@Query("store_id") storeId: Int,
		@Query("page") page: Int,
	): MABaseResponse<ResponseReviewsWithStats>

}
