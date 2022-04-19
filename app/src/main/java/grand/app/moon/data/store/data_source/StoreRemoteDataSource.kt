package grand.app.moon.data.store.data_source

import android.util.Log
import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreFilterRequest
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(private val apiService: StoreServices) :
  BaseRemoteDataSource() {

  suspend fun follow(storeRequest: FollowStoreRequest) = safeApiCall {
    apiService.follow(storeRequest)
  }

  suspend fun storeDetails(id: Int) = safeApiCall {
    apiService.storeDetails(id)
  }

  private val TAG = "StoreRemoteDataSource"
  suspend fun getStores(request: StoreFilterRequest) = safeApiCall {
    Log.d(TAG, "getStores: ")
    apiService.getStores(getParameters(request))
  }

  suspend fun getFavouriteStores(page: Int) = safeApiCall {
    apiService.getFavouriteStores(page)
  }


}