package grand.app.moon.data.home.data_source.remote

import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(private val apiService: HomeServices) :
  BaseRemoteDataSource() {

  suspend fun home() = safeApiCall {
    apiService.home()
  }

  suspend fun getAppGlobalAnnouncement() = safeApiCall {
    apiService.getAppGlobalAnnouncement()
  }

  suspend fun getAnnouncement() = safeApiCall {
    apiService.getAnnouncement()
  }

  suspend fun stories(categoryId: Int?) = safeApiCall {
    apiService.stories(categoryId)
  }

  suspend fun getCategories() = safeApiCall {
    apiService.getCategories()
  }

  suspend fun getCategories2() = safeApiCall {
    apiService.getCategories2()
  }

  suspend fun getCategoryDetails(id:Int) = safeApiCall {
    apiService.getCategoryDetails(id)
  }



}