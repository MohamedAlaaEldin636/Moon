package grand.app.moon.data.store.data_source

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.store.entity.FollowStoreRequest
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(private val apiService: StoreServices) :
  BaseRemoteDataSource() {

  suspend fun follow(storeRequest: FollowStoreRequest) = safeApiCall {
    apiService.follow(storeRequest)
  }

  suspend fun storeDetails(id: Int) = safeApiCall {
    apiService.storeDetails(id)
  }


  suspend fun getFavouriteStores(page: Int) = safeApiCall {
    apiService.getFavouriteStores(page)
  }


}