package grand.app.moon.data.packages

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.packages.BasePagination
import grand.app.moon.domain.packages.ResponseBecomeShopPackage
import grand.app.moon.domain.utils.BaseResponse
import javax.inject.Inject

class PackagesRemoteDataSource @Inject constructor(private val apiService: PackagesServices) : BaseRemoteDataSource() {

	/*suspend fun getDetails(id: Int, type: Int) = safeApiCall {
		apiService.details(id, type)
	}*/

	suspend fun getBecomeShopPackages(page: Int) = safeApiCall { apiService.getBecomeShopPackages(page) }

	suspend fun subscribeToBecomeShopPackage(packageId: Int) = safeApiCall { apiService.subscribeToBecomeShopPackage(packageId) }

}