package grand.app.moon.data.home.data_source.remote

import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(private val apiService: HomeServices) :
  BaseRemoteDataSource() {

  suspend fun homeStudent() = safeApiCall {
    apiService.homeStudent()
  }
}