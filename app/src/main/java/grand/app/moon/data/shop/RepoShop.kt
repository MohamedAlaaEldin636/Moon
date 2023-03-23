package grand.app.moon.data.shop

import android.content.Context
import androidx.paging.PagingData
import dagger.hilt.android.qualifiers.ApplicationContext
import grand.app.moon.core.extenstions.*
import grand.app.moon.data.home.repository.HomeRepositoryImpl
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.ads.DynamicFilterProperty
import grand.app.moon.domain.ads.ResponseFilterProperties
import grand.app.moon.domain.ads.toResponseFilterProperties
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.packages.PackageType
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.map
import grand.app.moon.domain.utils.toFailureStatus
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.mapToNullSuccess
import grand.app.moon.extensions.orZero
import grand.app.moon.helpers.paging.*
import grand.app.moon.presentation.home.AllStoresFragment
import grand.app.moon.presentation.home.FilterAllFragment
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragment
import grand.app.moon.presentation.home.models.Interaction
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.models.TypeSearchResult
import grand.app.moon.presentation.map.MapOfDataFragment
import grand.app.moon.presentation.map.model.ResponseMapData
import grand.app.moon.presentation.map.model.toResponseMapData
import grand.app.moon.presentation.myAds.model.ItemStatsInAdvDetails
import grand.app.moon.presentation.myStore.ItemWorkingHours2
import grand.app.moon.presentation.myStore.model.ResponseCountry
import grand.app.moon.presentation.stats.models.ItemStoreStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoShop @Inject constructor(
	private val remoteDataSource: ShopRemoteDataSource,
	private val useCaseCountries: CountriesUseCase,
	@ApplicationContext private val appContext: Context,
	private val appPreferences: AppPreferences,
	private val homeRepository: HomeRepository
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

	suspend fun getMyStoreCategoriesAndSubCategoriesAllPaginationPages(): Resource<BaseResponse<Pair<List<IdAndName>, List<ResponseStoreSubCategory>>?>> {
		val allCategories = BasePaging.getAllPages {
			remoteDataSource.getMyCategories(it)
		}

		if (allCategories is Resource.Success) {
			val allSubCategories = BasePaging.getAllPages {
				remoteDataSource.getMySubCategories(it)
			}

			if (allSubCategories is Resource.Success) {
				return allCategories.mapSuccess { baseResponse ->
					baseResponse.map { list ->
						Pair(
							list.orEmpty(),
							allSubCategories.value.data.orEmpty()
						)
					}
				}
			}

			return allSubCategories.mapToNullSuccess()
		}

		return allCategories.mapToNullSuccess()
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

	suspend fun addReviewForStore(
		storeId: Int,
		rate: Int?,
		review: String
	) = remoteDataSource.addReviewForStore(storeId, rate, review)

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
				response?.reviews?.copy(
					data = response.reviews?.data?.map { clientReview ->
						clientReview.copy(
							parentResponse = response.copy(
								reviews = null
							)
						)
					}
				)
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

	suspend fun getReviewsForStore(storeId: Int) = remoteDataSource.getReviewsForStore(storeId, 1).toResource()

	fun getReviewsForAdvPaging(advertisementId: Int) = BasePaging.createFlowViaPager {
		remoteDataSource.getReviewsForAdv(advertisementId, it).mapImmediate { maBaseResponse ->
			maBaseResponse.map { response ->
				response?.reviews?.copy(
					data = response.reviews?.data?.map { clientReview ->
						clientReview.copy(
							parentResponse = response.copy(
								reviews = null
							)
						)
					}
				)
			}
		}
	}

	fun getReviewsForStorePaging(storeId: Int) = BasePaging.createFlowViaPager {
		remoteDataSource.getReviewsForStore(storeId, it).mapImmediate { maBaseResponse ->
			maBaseResponse.map { response ->
				response?.reviews?.copy(
					data = response.reviews?.data?.map { clientReview ->
						clientReview.copy(
							parentResponse = response.copy(
								reviews = null
							)
						)
					}
				)
			}
		}
	}

	/*fun getHomeExplores() = BasePaging.createFlowViaPager {
		remoteDataSource.getHomeExplores(it)
	}*/

	fun getHomeExplores(
		minimumPageNumber: Int = 1,
	) = BasePaging.createFlowViaPager(minimumPageNumber) {
		remoteDataSource.getHomeExplores(it)
	}

	fun getHomeExplores2(
		minimumPageNumber: Int = 1,
	) = BasePaging.createFlowViaPager(minimumPageNumber) {
		remoteDataSource.getHomeExplores2(it)
	}

	fun getHomeExplores3() = BasePaging.createFlowViaPager {
		remoteDataSource.getHomeExplores2(it)
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

	fun getAllStoriesFollowing(
		transformation: (List<ResponseStory>) -> List<ResponseStory> = { list ->
			list.sortedBy {
				if (it.isSeen) 1 else 0
			}
		}
	) = BasePaging.createFlowViaPager {
		remoteDataSource.getAllStories(it).mapImmediate { baseResponse ->
			baseResponse.map { response ->
				response?.followedStoresStories?.let { stories ->
					val souqMoonStory: ResponseStory? = response.souqMoonStory?.let { souqMoonStory ->
						if (souqMoonStory.stories.isNullOrEmpty()) null else souqMoonStory.copy(isSouqMoonStory = true)
					}

					stories.copy(
						listOfNotNull(souqMoonStory) + transformation(stories.data.orEmpty())
					)
				}
			}
		}
	}

	fun getAllStoriesOther(
		transformation: (List<ResponseStory>) -> List<ResponseStory> = { list ->
			list.sortedBy {
				if (it.isSeen) 1 else 0
			}
		}
	) = BasePaging.createFlowViaPager {
		remoteDataSource.getAllStories(it).mapImmediate { baseResponse ->
			baseResponse.map { response ->
				response?.stories?.let { stories ->
					stories.copy(
						transformation(stories.data.orEmpty())
					)
				}
			}
		}
	}

	fun getStatusUsersHistory(
		type: String,
		userId: Int,
		advId: Int?,
	) = BasePaging.createFlowViaPager {
		remoteDataSource.getStatusUsersHistory(type, userId, advId, it)
	}

	suspend fun followStore(storeId: Int) = remoteDataSource.followStore(storeId)

	suspend fun deleteAccountPermanently() = remoteDataSource.deleteAccountPermanently()

	suspend fun likeExplore(exploreId: Int) = remoteDataSource.setExploreActionInteractive(
		exploreId, ExploreInteractions.LIKE.apiValue
	)

	suspend fun shareExplore(exploreId: Int) = remoteDataSource.setExploreActionInteractive(
		exploreId, ExploreInteractions.SHARE.apiValue
	)

	fun getSimpleUsersOfExploreLikes(id: Int): Flow<PagingData<SimpleUserListOfInteractionsFragment.Item>> = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getSimpleUsersOfExploreLikes(id, page).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.user?.name.orEmpty(),
						it.user?.image.orEmpty(),
					)
				}
			}
		}
	}

	/**
	 * - Might not be cached (saved locally) due to error in the background thread isa.
	 */
	suspend fun fetchAllCategoriesAndSaveThemLocallyIfPossible(retries: Int = 1) {
		repeat(retries.coerceAtLeast(0).inc()) {
			val resource = getAllAppCategoriesWithSubcategoriesAndBrands()

			if (resource is Resource.Success) {
				appContext.setCategoriesWithSubCategoriesAndBrands(resource.value.data, appPreferences)

				return
			}
		}
	}

	suspend fun getAnnouncementIfShouldAppearOrNull() = appPreferences.getAnnouncementLocally()

	suspend fun fetchAnnouncementAndSaveItLocally(retries: Int = 1) {
		repeat(retries.coerceAtLeast(0).inc()) {
			MyLogger.e("dhasuidhasi")

			val resource = remoteDataSource.getAnnouncement()


			MyLogger.e("dhasuidhasi $resource")

			if (resource is MAResult.Success) {
				MyLogger.e("dhasuidhasi 2")

				appPreferences.saveAnnouncementLocally(resource.value.data)

				return
			}

			delay(100)
		}
	}

	fun getCategoriesWithSubCategoriesAndBrands() = appContext.getCategoriesWithSubCategoriesAndBrands()

	suspend fun getAllAppCategoriesWithSubcategoriesAndBrands() = remoteDataSource
		.getAllAppCategoriesWithSubcategoriesAndBrands()

	fun saveAllAppCategoriesWithSubcategoriesAndBrandsLocally(list: List<ItemCategory>) = appContext.setCategoriesWithSubCategoriesAndBrands(list, appPreferences)

	suspend fun viewStoryInteractions(storyId: Int) = remoteDataSource.storyInteractions(
		storyId,
		Interaction.Story.VIEW
	)
	suspend fun shareStoryInteractions(storyId: Int) = remoteDataSource.storyInteractions(
		storyId,
		Interaction.Story.SHARE
	)
	suspend fun likeStoryInteractions(storyId: Int) = remoteDataSource.storyInteractions(
		storyId,
		Interaction.Story.LIKE
	)

	suspend fun getMapData(
		type: MapOfDataFragment.Type,
		categoryId: Int?,
		subCategoryId: Int?,
		propertyId: Int?,
	): Resource<BaseResponse<List<ResponseMapData>?>> {
		return when (type) {
			MapOfDataFragment.Type.STORE -> {
				remoteDataSource.getMapDataForStore(categoryId, subCategoryId, propertyId).mapSuccess { baseResponse ->
					baseResponse.map { list ->
						list?.map { it.toResponseMapData() }
					}
				}
			}
			MapOfDataFragment.Type.ADVERTISEMENT -> {
				remoteDataSource.getMapDataForAdv(categoryId, subCategoryId, propertyId).mapSuccess { baseResponse ->
					baseResponse.map { list ->
						list?.map { it.toResponseMapData() }
					}
				}
			}
		}
	}

	suspend fun favOrUnFavAdv(advId: Int) = remoteDataSource.favOrUnFavAdv(advId)

	suspend fun shareAdv(advId: Int) = remoteDataSource.shareAdv(advId)

	fun getCountriesWithCitiesWithAreas() = appContext.getCountriesWithCitiesWithAreas()
	fun setCountriesWithCitiesWithAreas(
		value: List<ResponseCountry>
	) = appContext.setCountriesWithCitiesWithAreas(value)

	fun getSelectedCountry() = appContext.getSelectedCountry()
	fun setSelectedCountry(value: ResponseCountry) = appContext.setSelectedCountry(value)

	suspend fun getFilterProperties(
		categoryId: Int?,
		subCategoryId: Int?,
	): Resource<BaseResponse<ResponseFilterProperties?>> {
		return if (categoryId == null) {
			Resource.Success(BaseResponse(ResponseFilterProperties(), "", 200))
		}else {
			remoteDataSource.getDynamicFilterProperties(categoryId, subCategoryId).mapSuccess { baseResponse ->
				baseResponse.map {
					it?.toResponseFilterProperties()
				}
			}
		}
	}

	fun getFilterResults(filter: FilterAllFragment.Filter) = BasePaging.createFlowViaPager {
		remoteDataSource.getFilterResults(it, filter)
	}

	fun getAllStores(filter: AllStoresFragment.Filter) = BasePaging.createFlowViaPager {
		remoteDataSource.getAllStores(it, filter)
	}

	suspend fun getCategoryDetails(categoryId: Int) = remoteDataSource.getCategoryDetails(categoryId)

	suspend fun getCategoryStories(categoryId: Int) = remoteDataSource.getCategoryStories(categoryId)

	fun getAllAds(filter: FilterAllFragment.Filter) = BasePaging.createFlowViaPager {
		remoteDataSource.getAllAds(it, filter)
	}

	fun getAllAdsHandlingChanges(filter: FilterAllFragment.Filter) = BasePaging.createFlowViaPager {
		remoteDataSource.getAllAdsAsResponseBody(it, filter)
	}

	suspend fun getAllAdsOfCategorySlider(filter: FilterAllFragment.Filter) =
		remoteDataSource.getAllAdsOfCategory(1, filter).mapImmediate { maBaseResponse ->
			maBaseResponse.map { response ->
				response?.let {
					it.slider to it.adsCount
				}
			}
		}

	fun getAllAdsOfCategory(filter: FilterAllFragment.Filter) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getAllAdsOfCategory(page, filter).mapImmediate { maBaseResponse ->
			maBaseResponse.map { response ->
				response?.advertisements?.map {
					it.copy(
						adsCount = response.adsCount,
						slider = response.slider
					)
				}
			}
		}
	}

	suspend fun getAdvDetailsFromView(id: Int) = remoteDataSource.getAdvDetails(id, false)

	suspend fun getAdvDetailsFromSearch(id: Int) = remoteDataSource.getAdvDetails(id, true)

	suspend fun getStoreDetailsFromView(id: Int) = remoteDataSource.getStoreDetails(id, false)

	suspend fun getStoreDetailsFromSearch(id: Int) = remoteDataSource.getStoreDetails(id, true)

	suspend fun getAdvReportingReason() = remoteDataSource.getAdvReportingReason()

	suspend fun reportAdv(
		advertisementId: Int,
		reasonId: Int,
	) = remoteDataSource.reportAdv(advertisementId, reasonId)

	suspend fun shareStore(storeId: Int) = remoteDataSource.shareStore(storeId)

	fun getStoreViews(id: Int) = BasePaging.createFlowViaPager {
		remoteDataSource.getStoreViews(id).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
						it.totalViews.orZero(),
						it.createdAt.orEmpty()
					)
				}
			}
		}
	}

	fun getStoreFollowers(id: Int) = BasePaging.createFlowViaPager {
		remoteDataSource.getStoreFollowers(id).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
					)
				}
			}
		}
	}

	fun getStoryViews(id: Int) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getStoryViews(id, page).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
						it.count,
						it.createdAt,
						it.email,
						it.phone,
						it.countryCode
					)
				}
			}
		}
	}
	fun getStoryLikes(id: Int) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getStoryLikes(id, page).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
						it.count,
						it.createdAt,
						it.email,
						it.phone,
						it.countryCode
					)
				}
			}
		}
	}
	fun getStoryShares(id: Int) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getStoryShares(id, page).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
						it.count,
						it.createdAt,
						it.email,
						it.phone,
						it.countryCode
					)
				}
			}
		}
	}
	fun getExploreComments(id: Int) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getExploreComments(id, page).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
						it.count,
						it.createdAt,
						it.email,
						it.phone,
						it.countryCode
					)
				}
			}
		}
	}
	fun getExploreLikes(id: Int) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getExploreLikes(id, page).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
						it.count,
						it.createdAt,
						it.email,
						it.phone,
						it.countryCode
					)
				}
			}
		}
	}
	fun getExploreShares(id: Int) = BasePaging.createFlowViaPager { page ->
		remoteDataSource.getExploreShares(id, page).mapImmediate { maBaseResponse ->
			maBaseResponse.map { maBasePaging ->
				maBasePaging?.map {
					SimpleUserListOfInteractionsFragment.Item(
						it.id.orZero(),
						it.name.orEmpty(),
						it.image.orEmpty(),
						it.count,
						it.createdAt,
						it.email,
						it.phone,
						it.countryCode
					)
				}
			}
		}
	}

	suspend fun getStoreReportingReasons() = remoteDataSource.getStoreReportingReasons()

	suspend fun getStoreBlockingReasons() = remoteDataSource.getStoreBlockingReasons()

	suspend fun reportStore(storeId: Int, reasonId: Int) = remoteDataSource.reportStore(storeId, reasonId)

	suspend fun blockStore(storeId: Int, reasonId: Int) = remoteDataSource.blockStore(storeId, reasonId)

	suspend fun getChatAgent() = remoteDataSource.getChatAgent()

	fun getFollowedStores(categoryId: Int?) = BasePaging.createFlowViaPager {
		remoteDataSource.getFollowedStores(categoryId, it)
	}
	
	suspend fun interactionForAdWhatsApp(advId: Int) = remoteDataSource.interactionForAdv(
		advId, ShopRemoteDataSource.TypeAdOrStoreInteraction.WHATSAPP
	)
	suspend fun interactionForAdCall(advId: Int) = remoteDataSource.interactionForAdv(
		advId, ShopRemoteDataSource.TypeAdOrStoreInteraction.CALL
	)
	suspend fun interactionForAdChat(advId: Int) = remoteDataSource.interactionForAdv(
		advId, ShopRemoteDataSource.TypeAdOrStoreInteraction.CHAT
	)
	suspend fun interactionForStoreWhatsApp(storeId: Int) = remoteDataSource.interactionForStore(
		storeId, ShopRemoteDataSource.TypeAdOrStoreInteraction.WHATSAPP
	)
	suspend fun interactionForStoreCall(storeId: Int) = remoteDataSource.interactionForStore(
		storeId, ShopRemoteDataSource.TypeAdOrStoreInteraction.CALL
	)
	suspend fun interactionForStoreChat(storeId: Int) = remoteDataSource.interactionForStore(
		storeId, ShopRemoteDataSource.TypeAdOrStoreInteraction.CHAT
	)

	suspend fun getExploreDetails(id: Int) = remoteDataSource.getExploreDetails(id)

	suspend fun sendCode(countryCode: String, phone: String) = remoteDataSource.sendCode(countryCode, phone)

	suspend fun getRestOfDaysInMyPackageIfHaveOneOrZeroInstead() =
		remoteDataSource.getRestOfDaysInMyPackageIfHaveOneOrZeroInstead()

}

enum class ExploreInteractions(val apiValue: Int) {
	LIKE(1), SHARE(2)
}

data class CitiesAndStoreCategoriesAndSubCategories(
	val cities: List<Country> = emptyList(),
	val categories: List<IdAndName> = emptyList(),
	val subCategories: List<IdAndName> = emptyList(),
)
