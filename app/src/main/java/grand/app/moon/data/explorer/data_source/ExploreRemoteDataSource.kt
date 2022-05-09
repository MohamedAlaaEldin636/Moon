package grand.app.moon.data.explorer.data_source

import android.util.Log
import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.explore.entity.ExploreAction
import javax.inject.Inject

class ExploreRemoteDataSource @Inject constructor(private val apiService: ExploreServices) :
  BaseRemoteDataSource() {

  private  val TAG = "ExploreRemoteDataSource"
  suspend fun explores(page: Int) = safeApiCall {
    apiService.explores(page)
  }

  suspend fun setExploreAction(page: ExploreAction) = safeApiCall {
    Log.d(TAG, "setExploreAction: ")
    apiService.setExploreAction(page)
  }

  suspend fun setComment(page: ExploreAction) = safeApiCall {
    apiService.setComment(page)
  }

  suspend fun deleteComment(page: Int) = safeApiCall {
    apiService.deleteComment(page)
  }


  suspend fun getComments(id: Int,page: Int) = safeApiCall {
    apiService.getComments(id,page)
  }

  suspend fun getUsers(id: Int,page: Int) = safeApiCall {
    apiService.getUsers(id,page)
  }
}