package grand.app.moon.data.packages

import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.domain.myStore.ResponseMyStoreDetails
import grand.app.moon.domain.myStore.ResponseSuccessPackageForBecomeShop
import grand.app.moon.domain.packages.BasePagination
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.presentation.myStore.model.ResponseCountry
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PackagesServices {

	@GET("v1/packages?type=shops")
	suspend fun getBecomeShopPackages(
		@Query("page") page: Int
	): MABaseResponse<MABasePaging<ResponsePackage>>/*BaseResponse<BasePagination<ResponseBecomeShopPackage>?>*/

	@GET("v1/packages?type=premium_advertisements")
	suspend fun getAdsPromotionPackagesPaging(
		@Query("page") page: Int
	): MABaseResponse<MABasePaging<ResponsePackage>>

	@GET("v1/packages?type=premium_shops")
	suspend fun getShopsPromotionPackagesPaging(
		@Query("page") page: Int
	): MABaseResponse<MABasePaging<ResponsePackage>>

	@GET("v1/packages?type=shops")
	suspend fun getBecomeShopPackagesPrimitivePagination(
		@Query("page") page: Int
	): BaseResponse<BasePagination<ResponsePackage>?>

	@GET("v1/packages/payment/success?type=shops")
	suspend fun subscribeToBecomeShopPackage(
		@Query("package_id") packageId: Int
	): BaseResponse<ResponseSuccessPackageForBecomeShop?>

	@GET("v1/packages/payment/success?type=premium_advertisements")
	suspend fun subscribeToMakeAdvertisementPremiumPackage(
		@Query("advertisement_id") advertisementId: Int,
		@Query("package_id") packageId: Int,
	): BaseResponse<Any?>

	@GET("v1/packages/payment/success?type=premium_shops")
	suspend fun subscribeToMakeShopPremiumPackage(
		@Query("package_id") packageId: Int,
	): BaseResponse<Any?>

	@GET("v1/packages/my-package")
	suspend fun getMyPackage(
		@Query("type") type: String,
		@Header("accept") accept: String = "application/json"
	): BaseResponse<ResponsePackage?>

	@GET("v1/profile")
	suspend fun getShopInfo(): BaseResponse<ResponseMyStoreDetails?>

	@GET("v1/countries")
	suspend fun getCountries(): BaseResponse<List<ResponseCountry>?>

	// should ad country codes as well isa.
	@Multipart
	@POST("v1/stores")
	suspend fun addOrUpdateStore(
		@Part images: List<MultipartBody.Part>,
		@Part("name") storeName: RequestBody,
		@Part("nickname") userName: RequestBody,
		@Part("city_id") cityId: Int,
		@Part("area_id") areaId: Int,
		@Part("latitude") latitude: RequestBody,
		@Part("longitude") longitude: RequestBody,
		@Part("address") address: RequestBody,
		@Part("description") description: RequestBody,
		@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
	): BaseResponse<Any?>

}
