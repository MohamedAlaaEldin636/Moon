package grand.app.moon.data.packages

import grand.app.moon.domain.packages.PackageType
import grand.app.moon.helpers.paging.BasePaging
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryPackages @Inject constructor(
	private val packagesRemoteDataSource: PackagesRemoteDataSource
) {

	fun getBecomeShopPackages() = BasePaging.createFlowViaPager {
		packagesRemoteDataSource.getBecomeShopPackages(it)
	}
	fun getAdsPromotionPackagesPaging() = BasePaging.createFlowViaPager {
		packagesRemoteDataSource.getAdsPromotionPackagesPaging(it)
	}
	fun getShopsPromotionPackagesPaging() = BasePaging.createFlowViaPager {
		packagesRemoteDataSource.getShopsPromotionPackagesPaging(it)
	}

	suspend fun getBecomeShopPackagesSuspend(page: Int = 1) = packagesRemoteDataSource.getBecomeShopPackagesPrimitivePagination(page)

	suspend fun subscribeToBecomeShopPackage(packageId: Int) = packagesRemoteDataSource.subscribeToBecomeShopPackage(packageId)
	suspend fun subscribeToMakeAdvertisementPremiumPackage(advertisementId: Int, packageId: Int) =
		packagesRemoteDataSource.subscribeToMakeAdvertisementPremiumPackage(advertisementId, packageId)
	suspend fun subscribeToMakeShopPremiumPackage(packageId: Int) =
		packagesRemoteDataSource.subscribeToMakeShopPremiumPackage(packageId)

	suspend fun getMyPackageForPremiumAds() = packagesRemoteDataSource.getMyPackage(PackageType.PREMIUM_ADS)
	suspend fun getMyPackageForPremiumShops() = packagesRemoteDataSource.getMyPackage(PackageType.PREMIUM_SHOPS)
	suspend fun getMyPackageOfBeingShop() = packagesRemoteDataSource.getMyPackage(PackageType.BECOME_SHOP)

}
