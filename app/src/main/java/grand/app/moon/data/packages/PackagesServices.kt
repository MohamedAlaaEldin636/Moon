package grand.app.moon.data.packages

import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.packages.BasePagination
import grand.app.moon.domain.packages.ResponseBecomeShopPackage
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PackagesServices {

	@GET("v1/packages?type=shops")
	suspend fun getBecomeShopPackages(
		@Query("page") page: Int
	): BaseResponse<BasePagination<List<ResponseBecomeShopPackage>>?>

	@GET("v1/packages/payment/success?type=0")
	suspend fun subscribeToBecomeShopPackage(
		@Query("package_id") packageId: Int
	): BaseResponse<Any?>

}
