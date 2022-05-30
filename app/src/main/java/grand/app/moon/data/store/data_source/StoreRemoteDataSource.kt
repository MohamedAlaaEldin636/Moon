package grand.app.moon.data.store.data_source

import android.util.Log
import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.store.entity.*
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(private val apiService: StoreServices) :
  BaseRemoteDataSource() {

  suspend fun follow(storeRequest: FollowStoreRequest) = safeApiCall {
    apiService.follow(storeRequest)
  }
  suspend fun unBlock(storeRequest: FollowStoreRequest) = safeApiCall {
    apiService.unBlock(storeRequest)
  }

  suspend fun unBlock(storeRequest: ArrayList<Int>) = safeApiCall {
    apiService.unBlock(storeRequest)
  }



  suspend fun storeDetails(id: Int,type: Int) = safeApiCall {
    apiService.storeDetails(id,type)
  }

  private val TAG = "StoreRemoteDataSource"
  suspend fun getStores(request: StoreFilterRequest) = safeApiCall {
    Log.d(TAG, "getStores: ")
    apiService.getStores(getParameters(request))
  }

  suspend fun storyAction(request: StoryRequest) = safeApiCall {
    Log.d(TAG, "getStores: ")
    apiService.storyAction(request)
  }



  suspend fun getFavouriteStores(page: Int) = safeApiCall {
    apiService.getFavouriteStores(page)
  }

  suspend fun getBlockedStores(page: Int) = safeApiCall {
    apiService.getBlockedStores(page)
  }

  suspend fun getWhatsappStores(page: Int) = safeApiCall {
    apiService.getWhatsappStores(page)
  }


  suspend fun report(page: ReportStoreRequest) = safeApiCall {
    apiService.report(page)
  }

  suspend fun share(page: ShareRequest) = safeApiCall {
    apiService.share(page)
  }

  suspend fun getUsersViewFollowing(storeId: Int ,type: String) = safeApiCall {
    apiService.getUsersViewFollowing(storeId,type)
  }



}