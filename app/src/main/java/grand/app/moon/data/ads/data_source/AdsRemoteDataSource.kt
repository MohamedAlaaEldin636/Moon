package grand.app.moon.data.ads.data_source

import android.util.Log
import grand.app.moon.BuildConfig
import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
import grand.app.moon.domain.home.models.review.ReviewRequest
import javax.inject.Inject

class AdsRemoteDataSource @Inject constructor(private val apiService: AdsServices) :
  BaseRemoteDataSource() {

  private val TAG = "AdsRemoteDataSource"
  suspend fun getDetails(id: Int, type: Int) = safeApiCall {
    apiService.details(id, type)
  }

  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest) = safeApiCall {
    apiService.favourite(addFavouriteAdsRequest)
  }

  suspend fun getProfileAdsList(page: Int, type: Int?) = safeApiCall {
    Log.d(TAG, "getProfileAdsList: $page")
    Log.d(TAG, "getProfileAdsList: $type")
    apiService.getProfileAdsList(page, type)
  }
  /*
      @Query("type") type: Int?,
    @Query("category_id") categoryId: Int?,
    @Query("sub_category_id") subCategoryId: Int?,
    @Query("order_by") orderBy: Int? = 1,
    @Query("store_id") storeId: Int?,
    @Query("page") page: Int?,
   */

  suspend fun getAdsList(
    type: Int?,
    categoryId: Int?,
    subCategoryId: Int?,
    orderBy: Int?,
    storeId: Int?,
    search: String = "",
    page: Int?
  ) = safeApiCall {
    apiService.getAds(type, categoryId, subCategoryId, orderBy, storeId, search, page)
  }


  suspend fun getAdsSubCategory(
    type: Int?,
    categoryId: Int?,
    subCategoryId: Int?,
    orderBy: Int?,
    storeId: Int?,
    search: String = "",
    properties: ArrayList<Property>?,
    page: Int?
  ) = safeApiCall {
    var propery: Int? = null
    properties?.let {
      if(it.isNotEmpty()) propery = it[0].id
    }
    Log.d(TAG, "getAdsSubCategory: $propery")
    apiService.getAdsSubCategory(type, categoryId, subCategoryId, orderBy, storeId, search,propery, page)
  }


  suspend fun getReviews(page: Int, storeId: String?, advertisementId: String?) = safeApiCall {
    apiService.getReviews(storeId, advertisementId, page)
  }

  suspend fun addReview(request: ReviewRequest) = safeApiCall {
    apiService.addReview(getParameters(request))
  }

  suspend fun setInteraction(request: InteractionRequest) = safeApiCall {
    apiService.setInteraction(request)
  }

  suspend fun filterDetails(categoryId: Int, subCategoryId: Int?) = safeApiCall {
    var url = "${BuildConfig.API_BASE_URL}v1/filter/details?category_id=$categoryId"
    if (subCategoryId != null && subCategoryId != -1) url += "&sub_category_id=$subCategoryId"
    apiService.filterDetails(url)
  }

  suspend fun filterResults(request: FilterResultRequest) = safeApiCall {
    apiService.filterResults(getParameters(request))
  }


}