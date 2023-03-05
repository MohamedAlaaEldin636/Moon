package grand.app.moon.data.shop

import com.google.gson.annotations.SerializedName
import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.core.di.module.RetrofitModule
import grand.app.moon.domain.ads.ResponseFilterDetails
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.presentation.home.models.*
import grand.app.moon.presentation.map.model.ResponseMapData
import grand.app.moon.presentation.map.model.ResponseMapDataForAdv
import grand.app.moon.presentation.map.model.ResponseMapDataForStore
import grand.app.moon.presentation.myAds.model.ResponseMyAdvDetails
import grand.app.moon.presentation.stats.models.ResponseGeneralStats
import grand.app.moon.presentation.stats.models.ResponseStoreStats
import grand.app.moon.presentation.stats.models.ResponseUserInGeneralStats
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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
	fun addExplore(
		@Part files: List<MultipartBody.Part>,
		@Header(RetrofitModule.HEADER_KEY_TIME_OUT_IN_MINUTES) infiniteTimeout: String = 30.toString(),
	): Call<BaseResponse<Any?>>

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
		@Part("story_link_type") storyLinkType: RequestBody,
		@Part("highlight") storyType: RequestBody,
		@Part file: List<MultipartBody.Part>,
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

	@GET("v1/explores")
	suspend fun getHomeExplores(
		@Query("page") page: Int,
	): MABaseResponse<MABasePaging<ItemHomeExplore>>

	@GET("v1/explores")
	suspend fun getHomeExplores2(
		@Query("page") page: Int,
	): MABaseResponse<MABasePaging<ItemHomeExplore>>

	@GET("v1/profile/statistics")
	suspend fun getStoreStats(
		@QueryMap map: Map<String, String>,
		@Query("statistics_type") statisticsType: String = "store",
	): BaseResponse<ResponseStoreStats?>
	@GET("v1/statistics/show")
	suspend fun getGeneralStatsForStoreStats(
		@Query("type") type: String,
		@Query("statistics_type") statisticsType: String = "store",
	): BaseResponse<ResponseGeneralStats?>
	@GET("v1/statistics/show")
	suspend fun getGeneralStatsForStoreStatsUsers(
		@Query("type") type: String,
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>,
		@Query("statistics_type") statisticsType: String = "store",
	): MABaseResponse<ResponseGeneralStats?>

	@GET("v1/search")
	suspend fun getSearchResults(
		@Query("search") search: String,
		@Query("type") type: String,
		@Query("page") page: Int,
	): MABaseResponse<MABasePaging<ResponseSearchResult>>

	@GET("v1/settings")
	suspend fun getSettingsTypes(
		@Query("type") type: Int,
	): BaseResponse<List<ResponseSettings>?>

	@Multipart
	@POST("v1/contact-us")
	suspend fun setSettings(
		@Part("name") name: RequestBody,
		@Part("reason_id") reasonId: Int,
		@Part("message") message: RequestBody,
		@Part("phone") phone: RequestBody,
		@Part file: List<MultipartBody.Part>,
		@Part("contact_type") type: Int,
	): BaseResponse<Any?>

	@GET("v1/settings?type=13")
	suspend fun getContactUsData(): BaseResponse<List<ResponseContactUsData>?>

	@GET("v1/settings?type=3")
	suspend fun getAppSocialMedia(): BaseResponse<List<ResponseSocialMedia>?>

	@GET("v1/stories?all=1")
	suspend fun getAllStories(
		@Query("page") page: Int,
	): MABaseResponse<ResponseAllStories>

	//{{MoonTest}}/api/v1/statistics/user-history?type=views&advertisement_id=37797&statistics_type=advertisement&user_id=9797s
	//{{MoonTest}}/api/v1/statistics/user-history?type=views&statistics_type=store&user_id=9797s
	// pass type as string, statistics_type na hagebha isa., pass user_id, pass advId
	@GET("v1/statistics/user-history")
	suspend fun getStatusUsersHistory(
		@Query("type") type: String,
		@Query("statistics_type") statisticsType: String,
		@Query("user_id") userId: Int,
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>
	): MABaseResponse<MABasePaging<ResponseUserInGeneralStats>>

	@FormUrlEncoded
	@POST("v1/follow")
	suspend fun followStore(
		@Field("store_id") storeId: Int
	): BaseResponse<Any?>

	@POST("v1/auth/delete")
	suspend fun deleteAccountPermanently(): BaseResponse<Any?>

	@FormUrlEncoded
	@POST("v1/explores")
	suspend fun setExploreActionInteractive(
		@Field("explore_id") exploreId: Int,
		@Field("type") type: Int,
	): BaseResponse<Any?>

	@GET("v1/explores/{id}/likes")
	suspend fun getSimpleUsersOfExploreLikes(
		@Path("id") id: Int,
		@Query("page") page: Int)
	: MABaseResponse<MABasePaging<ResponseSimpleUserData>>

	@GET("v1/categories")
	suspend fun getAllAppCategoriesWithSubcategoriesAndBrands(): BaseResponse<List<ItemCategory>?>

	@FormUrlEncoded
	@POST("v1/stories")
	suspend fun storyInteractions(
		@Field("story_id") storyId: Int,
		@Field("type") interactionType: Int
	): BaseResponse<Any?>

	@GET("v1/map")
	suspend fun getMapDataForStore(
		@Query("type") type: String,
		@QueryMap map: Map<String, String>,
	): BaseResponse<List<ResponseMapDataForStore>?>
	@GET("v1/map")
	suspend fun getMapDataForAdv(
		@Query("type") type: String,
		@QueryMap map: Map<String, String>,
	): BaseResponse<List<ResponseMapDataForAdv>?>

	@FormUrlEncoded
	@POST("v1/favorite")
	suspend fun favOrUnFavAdv(
		@Field("advertisement_id") advId: Int,
	): BaseResponse<Any?>

	@GET("v1/filter/details")
	suspend fun getDynamicFilterProperties(
		@Query("category_id") categoryId: Int,
		//@Query("sub_category_id") subCategoryId: String,
		@QueryMap map: Map<String, String>
	): BaseResponse<ResponseFilterDetails?>

	@GET("v1/filter/results")
	suspend fun getFilterResults(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>
	): MABaseResponse<MABasePaging<ItemAdvertisementInResponseHome>>

	@GET("v1/stores")
	suspend fun getAllStores(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>
	): MABaseResponse<MABasePaging<ItemStoreInResponseHome>>

	@GET("v1/sub-categories")
	suspend fun getCategoryDetails(
		@Query("category_id") categoryId: Int?,
	): BaseResponse<ResponseCategoryDetails?>

	@GET("v1/stories")
	suspend fun getCategoryStories(
		@Query("category_id") categoryId: Int
	): BaseResponse<ResponseHomeStories?>

	@GET("v1/advertisements")
	suspend fun getAllAds(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>
	): MABaseResponse<MABasePaging<ItemAdvertisementInResponseHome>>

	@GET("v1/advertisements")
	suspend fun getAllAdsAsResponseBody(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>
	): ResponseBody

	@GET("v1/advertisements")
	suspend fun getAllAdsOfCategory(
		@Query("page") page: Int,
		@QueryMap map: Map<String, String>
	): MABaseResponse<ResponseAllAdsOfCategory>

	@FormUrlEncoded
	@POST("v1/share")
	suspend fun shareAdv(
		@Field("advertisement_id") advId: Int,
	): BaseResponse<Any?>

	@GET("v1/advertisements/{id}")
	suspend fun getAdvDetails(
		@Path("id") id: Int,
		@Query("type") type: Int,
	): BaseResponse<ResponseMyAdvDetails?>

	@GET("v1/settings?type=6")
	suspend fun getAdvReportingReason(): BaseResponse<List<ResponseReason>?>

	@FormUrlEncoded
	@POST("v1/report")
	suspend fun reportAdv(
		@Field("advertisement_id") advertisementId: Int,
		@Field("reason_id") reasonId: Int,
	): BaseResponse<Any?>

	@GET("v1/stores/{id}")
	suspend fun getStoreDetails(
		@Path("id") id: Int,
		@Query("type") type: Int,
	): BaseResponse<ResponseStoreDetails?>

	@FormUrlEncoded
	@POST("v1/share")
	suspend fun shareStore(
		@Field("store_id") storeId: Int,
	): BaseResponse<Any?>

	@GET("v1/stores/{id}/views")
	suspend fun getStoreViews(
		@Path("id") id: Int,
	): MABaseResponse<MABasePaging<ResponseStoreViews>>

	@GET("v1/stores/{id}/followers")
	suspend fun getStoreFollowers(
		@Path("id") id: Int,
	): MABaseResponse<MABasePaging<ResponseStoreViews>>

	@GET("v1/settings?type=7")
	suspend fun getStoreReportingReasons(): BaseResponse<List<ResponseReason>?>

	@GET("v1/settings?type=8")
	suspend fun getStoreBlockingReasons(): BaseResponse<List<ResponseReason>?>

	@FormUrlEncoded
	@POST("v1/store/report_block")
	suspend fun reportOrBlockStore(
		@Field("store_id") storeId: Int,
		@Field("reason_id") reasonId: Int,
		@Field("type") type: Int,
	): BaseResponse<Any?>

}
