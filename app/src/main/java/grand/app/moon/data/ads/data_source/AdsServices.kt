package grand.app.moon.data.ads.data_source

import grand.app.moon.core.di.module.RetrofitModule
import grand.app.moon.domain.ads.ResponseFilterDetails
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.entity.SearchData
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.home.models.review.Reviews
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.http.Url

import retrofit2.http.GET


interface AdsServices {

  @GET("v1/advertisements/{id}")
  suspend fun details(@Path("id") id: Int, @Query("type") type: Int): BaseResponse<Advertisement>

  @POST("v1/favorite")
  suspend fun favourite(@Body addFavouriteAdsRequest: AddFavouriteAdsRequest): BaseResponse<*>


  @POST("v1/interactions")
  suspend fun setInteraction(@Body addFavouriteAdsRequest: InteractionRequest): BaseResponse<*>

  @GET("v1/profile/advertisements")
  suspend fun getProfileAdsList(
    @Query("page") page: Int,
    @Query("type") type: Int?
  ): BaseResponse<AdsListPaginateData>

  @GET
  suspend fun getAdsList(@Url url: String): BaseResponse<AdsListPaginateData>

  @GET("v1/advertisements")
  suspend fun getAds(
    @Query("type") type: Int?,
    @Query("category_id") categoryId: Int?,
    @Query("sub_category_id") subCategoryId: Int?,
    @Query("order_by") orderBy: Int? = 1,
    @Query("store_id") storeId: Int?,
    @Query("other_options") other_options: Int?,
    @Query("search") search: String?,
    @Query("page") page: Int?,
    ): BaseResponse<AdsListPaginateData>

  @GET("v1/advertisements")
  suspend fun getAdsSubCategory(
    @Query("type") type: Int?,
    @Query("category_id") categoryId: Int?,
    @Query("sub_category_id") subCategoryId: Int?,
    @Query("order_by") orderBy: Int? = 1,
    @Query("store_id") storeId: Int?,
    @Query("search") search: String?,
    @Query("properties[0][id]") properties: Int?,
    @Query("page") page: Int?,
  ): BaseResponse<SubCategoryResponse>

  @GET("v1/reviews")
  suspend fun getReviews(
    @Query("store_id") storeId: String?,
    @Query("advertisement_id") adsId: String?,
    @Query("page") page: Int
  ): BaseResponse<ReviewsPaginateData>


  @POST("v1/review")
  suspend fun addReview(@Body request: ReviewRequest): BaseResponse<*>
  @FormUrlEncoded
  @POST("v1/review")
  suspend fun addReview(
    @FieldMap map : Map<String, String>
  ): BaseResponse<*>


  @GET("v1/filter/details")
  suspend fun filterDetails(
    @Query("category_id") category_id: Int,
    @Query("sub_category_id") sub_category_id: Int
  ): BaseResponse<FilterDetails>

  @GET("v1/search")
  suspend fun search(
    @Query("search") search: String?,@Query("page")  page:Int
  ): BaseResponse<SearchData>



  @GET
  suspend fun filterDetails(
    @Url filterDetails: String,
  ): BaseResponse<FilterDetails>


  @GET("v1/filter/results")
  suspend fun filterResults(
    @QueryMap map: Map<String, String>
  ): BaseResponse<AdsListPaginateData>

	@GET("v1/filter/details")
	suspend fun getFilterDetails2(
		@Query("category_id") categoryId: String,
		@Query("sub_category_id") subCategoryId: String,
	): BaseResponse<ResponseFilterDetails?>

	@Multipart
	@POST("v1/my-advertisements")
	suspend fun addAdvertisement(
		@Part("category_id") category_id: Int,
		@Part("sub_category_id") sub_category_id: Int,
		@Part images: List<MultipartBody.Part>,
		@Part("latitude") latitude: RequestBody,
		@Part("longitude") longitude: RequestBody,
		@Part("address") address: RequestBody,
		@Part("city_id") city_id: Int,
		@Part("price") price: Int,
		@Part("is_negotiable") is_negotiable: Int,
		//@Part("brand_id") brand_id: Int,
		//@Part("description") description: RequestBody,
		@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
		@Header(RetrofitModule.HEADER_KEY_TIME_OUT_IN_MINUTES) infiniteTimeout: String = 30.toString()
	): BaseResponse<Any?>

}