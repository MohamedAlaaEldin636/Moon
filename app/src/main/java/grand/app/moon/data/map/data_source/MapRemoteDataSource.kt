package grand.app.moon.data.map.data_source

import android.util.Log
import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class MapRemoteDataSource @Inject constructor(private val apiService: MapServices) :
  BaseRemoteDataSource() {

  private  val TAG = "MapRemoteDataSource"
  suspend fun map(type: String) = safeApiCall {
    Log.d(TAG, "map: MapRemoteDataSource")
    apiService.map(type)
  }
}