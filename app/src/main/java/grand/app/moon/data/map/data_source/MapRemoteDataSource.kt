package grand.app.moon.data.map.data_source

import android.util.Log
import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class MapRemoteDataSource @Inject constructor(private val apiService: MapServices) :
  BaseRemoteDataSource() {

  private  val TAG = "MapRemoteDataSource"

  suspend fun mapStore(type: String) = safeApiCall {
    Log.d(TAG, "map: MapRemoteDataSource")
    apiService.mapStore(type)
  }

  suspend fun mapAds(type: String,property_id: String?,subCategoryId: String?,categoryId: String?) = safeApiCall {
    Log.d(TAG, "map: MapRemoteDataSource")
    apiService.mapAds(type,property_id,subCategoryId,categoryId)
  }
}