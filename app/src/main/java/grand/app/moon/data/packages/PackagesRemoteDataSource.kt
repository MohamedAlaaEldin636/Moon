package grand.app.moon.data.packages

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.packages.PackageType
import javax.inject.Inject

class PackagesRemoteDataSource @Inject constructor(private val apiService: PackagesServices) : BaseRemoteDataSource() {

	/*suspend fun getDetails(id: Int, type: Int) = safeApiCall {
		apiService.details(id, type)
	}*/

	suspend fun getBecomeShopPackages(page: Int) = safeApiCall2 { apiService.getBecomeShopPackages(page) }
	suspend fun getAdsPromotionPackagesPaging(page: Int) = safeApiCall2 { apiService.getAdsPromotionPackagesPaging(page) }
	suspend fun getShopsPromotionPackagesPaging(page: Int) = safeApiCall2 { apiService.getShopsPromotionPackagesPaging(page) }

	suspend fun getBecomeShopPackagesPrimitivePagination(page: Int) = safeApiCall { apiService.getBecomeShopPackagesPrimitivePagination(page) }

	suspend fun subscribeToBecomeShopPackage(packageId: Int) = safeApiCall { apiService.subscribeToBecomeShopPackage(packageId) }
	suspend fun subscribeToMakeAdvertisementPremiumPackage(advertisementId: Int, packageId: Int) = safeApiCall {
		apiService.subscribeToMakeAdvertisementPremiumPackage(advertisementId, packageId)
	}
	suspend fun subscribeToMakeShopPremiumPackage(packageId: Int) = safeApiCall {
		apiService.subscribeToMakeShopPremiumPackage(packageId)
	}

	suspend fun getMyPackage(type: PackageType) = safeApiCall {
		apiService.getMyPackage(type.apiValue)
	}

}