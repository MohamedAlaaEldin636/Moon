package grand.app.moon.data.shop

import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.map
import grand.app.moon.domain.utils.toFailureStatus
import grand.app.moon.extensions.mapToNullSuccess
import grand.app.moon.helpers.paging.*
import grand.app.moon.presentation.home.models.TypeSearchResult
import grand.app.moon.presentation.myAds.model.ItemStatsInAdvDetails
import grand.app.moon.presentation.myStore.ItemWorkingHours2
import grand.app.moon.presentation.stats.models.ItemStoreStats
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoShop @Inject constructor(
	private val remoteDataSource: ShopRemoteDataSource,
	private val useCaseCountries: CountriesUseCase,
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

	suspend fun getMySubCategoriesInAllPagesOfPagination(parentId: Int): Resource<BaseResponse<List<IdAndName>?>> = BasePaging.getAllPages {
		remoteDataSource.getMySubCategoriesWithParentId(parentId, it)
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

	fun getClientsReviewsForAdv(advertisementId: Int) = BasePaging.createFlowViaPager {
		remoteDataSource.getClientsReviewsForAdv(advertisementId, it)
	}

	suspend fun addReviewForAdv(
		advertisementId: Int,
		rate: Int?,
		review: String
	) = remoteDataSource.addReviewForAdv(advertisementId, rate, review)

	/*fun getExplores(
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
	}*/

	suspend fun getExploresRemainingCount(): Resource<BaseResponse<Int?>> = remoteDataSource.getExploresRemainingCount()

	fun getExplores2(
		from: String?,
		to: String?,
	) = BasePaging.createFlowViaPager { page ->
		//    fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
		remoteDataSource.getExplores(from, to, page)/*.mapImmediate { response ->
			response.map { innerResponse ->
				innerResponse?.explores?.map {
					it
				}
			}
		}*/
	}

	suspend fun getStoriesRemainingCount(): Resource<BaseResponse<Int?>> = remoteDataSource.getStoriesRemainingCount()

	fun getStories(
		from: String?,
		to: String?,
		storyType: StoryType?,
	) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getStories(from, to, storyType, page)
	}

	suspend fun getClientsReviewsSuspend(
		query: String?,
		from: String?,
		to: String?,
		page: Int,
	) = remoteDataSource.getClientsReviews(query, from, to, page)

	suspend fun getShopClientsReviews() = remoteDataSource.getShopClientsReviews(
		null, null, null, 1
	).toResource()
	fun getShopClientsReviewsPaging(
		query: String?,
		from: String?,
		to: String?,
	) = BasePaging.createFlowViaPager {
		remoteDataSource.getShopClientsReviews(query, from, to, it).mapImmediate { maBaseResponse ->
			maBaseResponse.map { response ->
				response?.reviews
			}
		}
	}

	suspend fun addExplore(files: List<MultipartBody.Part>) = remoteDataSource.addExplore(files)

	suspend fun deleteExplore(id: Int) = remoteDataSource.deleteExplore(id)

	suspend fun deleteStory(id: Int) = remoteDataSource.deleteStory(id)

	suspend fun addStory(
		file: MultipartBody.Part,
		storyLink: StoryLink,
		storyType: StoryType,
		name: String?,
		coverImage: MultipartBody.Part?,
	) = remoteDataSource.addStory(file, storyLink, storyType, name, coverImage)

	/**
	 * @param storeCategoryId `null` if NOT store
	 */
	suspend fun getCitiesAndStoreCategoriesAndSubCategoriesIfPossible(storeCategoryId: Int?): Resource<BaseResponse<CitiesAndStoreCategoriesAndSubCategories?>> {
		val resourceCities = useCaseCountries.getCities()

		if (storeCategoryId == null) {
			return resourceCities.mapSuccess { baseResponse ->
				baseResponse.map { CitiesAndStoreCategoriesAndSubCategories(it.orEmpty()) }
			}
		}

		return if (resourceCities is Resource.Success) {
			val resourceCategories = getMyCategoriesInAllPagesOfPagination()

			if (resourceCategories is Resource.Success) {
				val resourceSubCategories = getMySubCategoriesInAllPagesOfPagination(storeCategoryId)

				if (resourceSubCategories is Resource.Success) {
					resourceSubCategories.mapSuccess { baseResponse ->
						baseResponse.map {
							CitiesAndStoreCategoriesAndSubCategories(
								resourceCities.value.data.orEmpty(),
								resourceCategories.value.data.orEmpty(),
								resourceSubCategories.value.data.orEmpty(),
							)
						}
					}
				}else {
					resourceCategories.mapToNullSuccess()
				}
			}else {
				resourceCategories.mapToNullSuccess()
			}
		}else {
			resourceCities.mapToNullSuccess()
		}
	}

	suspend fun getMyAdvStats(
		advId: Int,
		type: ItemStatsInAdvDetails.Type
	) = remoteDataSource.getMyAdvStats(advId, type)

	fun getMyAdvStatsUsers(
		advId: Int,
		type: ItemStatsInAdvDetails.Type,
		query: String?,
		from: String?,
		to: String?,
	) = BasePaging.createFlowViaPager {
		remoteDataSource.getMyAdvStatsUsers(advId, type, it, query, from, to).mapImmediate { response ->
			response.map { stats ->
				stats?.users
			}
		}
	}

	suspend fun getReviewsForAdv(advertisementId: Int) =
		remoteDataSource.getReviewsForAdv(advertisementId, 1).toResource()

	fun getReviewsForAdvPaging(advertisementId: Int) = BasePaging.createFlowViaPager {
		remoteDataSource.getReviewsForAdv(advertisementId, 1).mapImmediate { maBaseResponse ->
			maBaseResponse.map { response ->
				response?.reviews
			}
		}
	}

	fun getHomeExplores() = BasePaging.createFlowViaPager {
		remoteDataSource.getHomeExplores(it)
	}

	suspend fun getStoreStats(
		from: String?,
		to: String?
	) = remoteDataSource.getStoreStats(from, to)

	suspend fun getGeneralStatsForStoreStats(
		type: ItemStoreStats.Type,
	) = remoteDataSource.getGeneralStatsForStoreStats(type)

	fun getGeneralStatsForStoreStatsUsers(
		type: ItemStoreStats.Type,
		query: String?,
		from: String?,
		to: String?,
	) = BasePaging.createFlowViaPager {
		remoteDataSource.getGeneralStatsForStoreStatsUsers(
			type,
			it,
			query,
			from,
			to
		).mapImmediate { response ->
			response.map { stats ->
				stats?.users
			}
		}
	}

	fun getSearchResults(search: String, type: TypeSearchResult) = BasePaging.createFlowViaPager {
		remoteDataSource.getSearchResults(search, type, it)
	}

	suspend fun getComplainsAndSuggestionsTypes() = remoteDataSource.getComplainsAndSuggestionsTypes()

	suspend fun getContactUsTypes() = remoteDataSource.getContactUsTypes()

	suspend fun setComplainsAndSuggestionsSettings(
		name: String,
		reasonId: Int,
		message: String,
		phone: String,
		image: MultipartBody.Part?,
	) = remoteDataSource.setComplainsAndSuggestionsSettings(name, reasonId, message, phone, image)

	suspend fun setContactUsSettings(
		name: String,
		reasonId: Int,
		message: String,
		phone: String,
		image: MultipartBody.Part?,
	) = remoteDataSource.setContactUsSettings(name, reasonId, message, phone, image)

	suspend fun getContactUsData() = remoteDataSource.getContactUsData()

	suspend fun getAppSocialMedia() = remoteDataSource.getAppSocialMedia()

}

data class CitiesAndStoreCategoriesAndSubCategories(
	val cities: List<Country> = emptyList(),
	val categories: List<IdAndName> = emptyList(),
	val subCategories: List<IdAndName> = emptyList(),
)
