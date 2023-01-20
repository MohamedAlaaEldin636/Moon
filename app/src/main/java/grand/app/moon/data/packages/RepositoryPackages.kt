package grand.app.moon.data.packages

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.packages.PackageType
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.BasePaging
import grand.app.moon.presentation.myStore.model.ResponseArea
import grand.app.moon.presentation.myStore.model.ResponseCity
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryPackages @Inject constructor(
	private val packagesRemoteDataSource: PackagesRemoteDataSource,
	private val accountRepository: AccountRepository,
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

	suspend fun getShopInfo() = packagesRemoteDataSource.getShopInfo()

	suspend fun getCountriesAndCitiesAndAreas() = packagesRemoteDataSource.getCountries()

	suspend fun getCitiesWithAreas() = packagesRemoteDataSource.getCountries().mapSuccess { response ->
		val countryId = accountRepository.getCountryId().firstOrNull()

		BaseResponse(
			response.data?.firstOrNull { it.id?.toString() == countryId }?.let {
				it.cities.orEmpty()
			},
			response.message,
			response.code
		)
	}

	suspend fun addOrUpdateStore(
		coverImage: MultipartBody.Part?,
		logoImage: MultipartBody.Part?,
		storeName: String,
		userName: String,
		cityId: Int,
		areaId: Int,
		latitude: String,
		longitude: String,
		address: String,
		description: String,
		advertisingWebsite: String?,
		email: String?,
		websiteLink: String?,
		adsPhone: String?,
		whatsappPhone: String?,
		taxNumber: String?,
	) = packagesRemoteDataSource.addOrUpdateStore(
		coverImage, logoImage, storeName, userName, cityId, areaId, latitude, longitude, address,
		description, advertisingWebsite, email, websiteLink, adsPhone, whatsappPhone, taxNumber
	)

}
