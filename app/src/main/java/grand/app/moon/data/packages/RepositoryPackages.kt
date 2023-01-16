package grand.app.moon.data.packages

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryPackages @Inject constructor(
	private val packagesRemoteDataSource: PackagesRemoteDataSource
) {

	suspend fun getBecomeShopPackages(page: Int = 0) = packagesRemoteDataSource.getBecomeShopPackages(page)

	suspend fun subscribeToBecomeShopPackage(packageId: Int) = packagesRemoteDataSource.subscribeToBecomeShopPackage(packageId)

}
