package grand.app.moon.data.packages

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.myStore.ResponseSuccessPackageForBecomeShop
import grand.app.moon.domain.packages.PackageType
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap
import javax.inject.Inject

class PackagesRemoteDataSource @Inject constructor(private val apiService: PackagesServices) : BaseRemoteDataSource() {

	/*suspend fun getDetails(id: Int, type: Int) = safeApiCall {
		apiService.details(id, type)
	}*/

	suspend fun getBecomeShopPackages(page: Int) = safeApiCall2 { apiService.getBecomeShopPackages(page) }
	suspend fun getAdsPromotionPackagesPaging(page: Int) = safeApiCall2 { apiService.getAdsPromotionPackagesPaging(page) }
	suspend fun getShopsPromotionPackagesPaging(page: Int) = safeApiCall2 { apiService.getShopsPromotionPackagesPaging(page) }

	suspend fun getBecomeShopPackagesPrimitivePagination(page: Int) = safeApiCall { apiService.getBecomeShopPackagesPrimitivePagination(page) }

	suspend fun subscribeToBecomeShopPackage(packageId: Int): Resource<BaseResponse<ResponseSuccessPackageForBecomeShop?>> = safeApiCall { apiService.subscribeToBecomeShopPackage(packageId) }
	suspend fun subscribeToMakeAdvertisementPremiumPackage(advertisementId: Int, packageId: Int) = safeApiCall {
		apiService.subscribeToMakeAdvertisementPremiumPackage(advertisementId, packageId)
	}
	suspend fun subscribeToMakeShopPremiumPackage(packageId: Int) = safeApiCall {
		apiService.subscribeToMakeShopPremiumPackage(packageId)
	}

	suspend fun getMyPackage(type: PackageType) = safeApiCall {
		apiService.getMyPackage(type.apiValue)
	}

	suspend fun getShopInfo() = safeApiCall {
		apiService.getShopInfo()
	}

	suspend fun getCountries() = safeApiCall {
		apiService.getCountries()
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
	) = safeApiCall {
		val images = listOfNotNull(coverImage, logoImage)

		val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()
		if (advertisingWebsite.isNullOrEmpty().not()) {
			map["advertising_website"] = advertisingWebsite.orEmpty().toRequestBody()
		}
		if (email.isNullOrEmpty().not()) {
			map["email"] = email.orEmpty().toRequestBody()
		}
		if (websiteLink.isNullOrEmpty().not()) {
			map["website"] = websiteLink.orEmpty().toRequestBody()
		}
		if (adsPhone.isNullOrEmpty().not()) {
			map["ads_phone"] = adsPhone.orEmpty().toRequestBody()
		}
		if (whatsappPhone.isNullOrEmpty().not()) {
			map["whatsapp_phone"] = whatsappPhone.orEmpty().toRequestBody()
		}
		if (taxNumber.isNullOrEmpty().not()) {
			map["tax_number"] = taxNumber.orEmpty().toRequestBody()
		}

		apiService.addOrUpdateStore(
			images,
			storeName.toRequestBody(),
			userName.toRequestBody(),
			cityId,
			areaId,
			latitude.toRequestBody(),
			longitude.toRequestBody(),
			address.toRequestBody(),
			description.toRequestBody(),
			map
		)
	}

}