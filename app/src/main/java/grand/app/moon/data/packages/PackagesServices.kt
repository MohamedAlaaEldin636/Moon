package grand.app.moon.data.packages

import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import grand.app.moon.domain.packages.BasePagination
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import retrofit2.http.GET
import retrofit2.http.Query

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
	): BaseResponse<Any?>

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
	): BaseResponse<ResponsePackage?>

}
