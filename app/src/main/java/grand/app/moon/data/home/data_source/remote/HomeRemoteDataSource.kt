package grand.app.moon.data.home.data_source.remote

import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(private val apiService: HomeServices) :
  BaseRemoteDataSource() {

  suspend fun home() = safeApiCall {
    apiService.home()
  }

  suspend fun stories() = safeApiCall {
    apiService.stories()
  }

  suspend fun getCategories() = safeApiCall {
    apiService.getCategories()
  }

  suspend fun getCategoryDetails(id:Int) = safeApiCall {
    apiService.getCategoryDetails(id)
  }



}