package grand.app.moon.data.shop

import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.toFailureStatus
import grand.app.moon.helpers.paging.*
import grand.app.moon.presentation.myStore.ItemWorkingHours2
import okhttp3.MultipartBody
import retrofit2.http.Part
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

	fun getMySubCategories() = BasePaging.createFlowViaPager {
		remoteDataSource.getMySubCategories(it)
	}
	suspend fun deleteSubCategory(id: Int) = remoteDataSource.deleteSubCategory(id)
	suspend fun createSubCategory(name: String, parentId: Int) = remoteDataSource.createSubCategory(name, parentId)
	suspend fun updateSubCategory(id: Int, name: String, parentId: Int) = remoteDataSource.updateSubCategory(id, name, parentId)

	/** Warning not a good approach but no time to use custom popup menu as popup window then adjusting
	 * width and height of that popup window as no time given by company for now. */
	suspend fun getMyCategoriesInAllPagesOfPagination(): Resource<BaseResponse<List<IdAndName>?>> {
		var page = 1
		val list = mutableListOf<IdAndName>()
		while (true) {
			when (val response = remoteDataSource.getMyCategories(page++)) {
				is MAResult.Failure -> {
					return Resource.Failure(
						response.toFailureStatus(),
						response.code,
						response.message,
					)
				}
				is MAResult.Success -> {
					list += response.value.data?.data.orEmpty()

					if (response.value.data?.links?.next.isNullOrEmpty()) {
						break
					}
				}
			}
		}

		return Resource.Success(
			BaseResponse(
				list.toList(),
				"",
				200
			)
		)
	}

	suspend fun getWorkingHours() = remoteDataSource.getWorkingHours()

	suspend fun saveWorkingHours(list: List<ItemWorkingHours2>) = remoteDataSource.saveWorkingHours(list)

	suspend fun getSocialMedia() = remoteDataSource.getSocialMedia()

	suspend fun saveSocialMedia(list: List<ResponseStoreSocialMedia>) = remoteDataSource.saveSocialMedia(list)

	fun getClientsReviews(
		query: String?,
		from: String?,
		to: String?,
	) = BasePaging.createFlowViaPager {
		remoteDataSource.getClientsReviews(query, from, to, it)
	}

	fun getExplores(
		from: String?,
		to: String?,
	) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getExplores(from, to, page).mapImmediate { response ->
			response.map { innerResponse ->
				innerResponse?.explores?.map {
					innerResponse.exploresRestCount to it
				}
			}
		}
	}

	suspend fun getClientsReviewsSuspend(
		query: String?,
		from: String?,
		to: String?,
		page: Int,
	) = remoteDataSource.getClientsReviews(query, from, to, page)

	suspend fun addExplore(files: List<MultipartBody.Part>) = remoteDataSource.addExplore(files)

	suspend fun deleteExplore(id: Int) = remoteDataSource.deleteExplore(id)

}
