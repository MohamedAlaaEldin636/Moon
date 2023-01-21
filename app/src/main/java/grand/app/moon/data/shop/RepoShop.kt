package grand.app.moon.data.shop

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.packages.PackageType
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.BasePaging
import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.presentation.myStore.model.ResponseArea
import grand.app.moon.presentation.myStore.model.ResponseCity
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoShop @Inject constructor(
	private val remoteDataSource: ShopRemoteDataSource,
) {

	fun getMyCategories() = BasePaging.createFlowViaPager {
		remoteDataSource.getMyCategories(it)
	}

	suspend fun deleteCategory(id: Int) = remoteDataSource.deleteCategory(id)

	suspend fun createCategory(name: String) = remoteDataSource.createCategory(name)

	suspend fun updateCategory(id: Int, name: String) = remoteDataSource.updateCategory(id, name)

}
