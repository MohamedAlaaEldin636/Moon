package grand.app.moon.data.home2

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.data.shop.ShopServices
import javax.inject.Inject

class Home2RemoteDataSource @Inject constructor(
	private val apiService: Home2Services
) : BaseRemoteDataSource() {

	suspend fun checkAvailabilityForStories() = safeApiCall { apiService.checkAvailabilityForStories() }

	suspend fun getNotAllStories() = safeApiCall { apiService.getNotAllStories() }

	suspend fun getAllStories() = safeApiCall { apiService.getAllStories() }

	suspend fun getHome() = safeApiCall { apiService.getHome() }

}
