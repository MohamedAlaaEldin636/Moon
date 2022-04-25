package grand.app.moon.data.ads.data_source

import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.home.models.review.Reviews
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.*
import retrofit2.http.Url

import retrofit2.http.GET


interface AdsServices {

  @GET("v1/advertisements/{id}")
  suspend fun details(@Path("id") id: Int, @Query("type") type: Int): BaseResponse<Advertisement>

  @POST("v1/favorite")
  suspend fun favourite(@Body addFavouriteAdsRequest: AddFavouriteAdsRequest): BaseResponse<*>

  @GET("v1/profile/advertisements")
  suspend fun getProfileAdsList(
    @Query("page") page: Int,
    @Query("type") type: Int
  ): BaseResponse<AdsListPaginateData>

  @GET
  suspend fun getAdsList(@Url url: String): BaseResponse<AdsListPaginateData>

  @GET
  suspend fun getAdsSubCategory(@Url url: String): BaseResponse<SubCategoryResponse>

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

  @GET("v1/filter/results")
  suspend fun filterResults(
    @QueryMap map: Map<String, String>
  ): BaseResponse<AdsListPaginateData>



}