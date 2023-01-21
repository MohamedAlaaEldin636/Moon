package grand.app.moon.data.shop

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.packages.PackageType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap
import javax.inject.Inject

class ShopRemoteDataSource @Inject constructor(private val apiService: ShopServices) : BaseRemoteDataSource() {

	/*suspend fun getDetails(id: Int, type: Int) = safeApiCall {
		apiService.details(id, type)
	}*/

	suspend fun getMyCategories(page: Int) = safeApiCall2 {
		apiService.getMyCategories(page)
	}

	suspend fun deleteCategory(id: Int) = safeApiCall {
		apiService.deleteCategory(id)
	}

	suspend fun createCategory(name: String) = safeApiCall {
		apiService.createCategory(name)
	}

	suspend fun updateCategory(id: Int, name: String) = safeApiCall {
		apiService.updateCategory(id, name)
	}

}
