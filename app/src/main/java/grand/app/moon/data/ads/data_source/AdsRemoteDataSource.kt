package grand.app.moon.data.ads.data_source

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.auth.entity.request.*
import javax.inject.Inject

class AdsRemoteDataSource @Inject constructor(private val apiService: AdsServices) :
  BaseRemoteDataSource() {

  suspend fun getDetails(id: Int,type: Int) = safeApiCall {
    apiService.details(id,type)
  }

  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest) = safeApiCall {
    apiService.favourite(addFavouriteAdsRequest)
  }

  suspend fun getProfileAdsList(page:Int , type: Int) = safeApiCall {
    apiService.getProfileAdsList(page,type)
  }

  suspend fun getAdsList(url : String) = safeApiCall {
    apiService.getAdsList(url)
  }

  suspend fun getAdsSubCategory(url : String) = safeApiCall {
    apiService.getAdsSubCategory(url)
  }


}