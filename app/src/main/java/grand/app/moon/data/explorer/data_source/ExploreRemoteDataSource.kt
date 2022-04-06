package grand.app.moon.data.explorer.data_source

import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class ExploreRemoteDataSource @Inject constructor(private val apiService: ExploreServices) :
  BaseRemoteDataSource() {

  suspend fun explores() = safeApiCall {
    apiService.explores()
  }
}