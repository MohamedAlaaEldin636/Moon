package grand.app.moon.data.shop

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.minLengthZerosPrefix
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.toStringOrEmpty
import grand.app.moon.helpers.paging.*
import grand.app.moon.presentation.home.models.TypeSearchResult
import grand.app.moon.presentation.myAds.model.ItemStatsInAdvDetails
import grand.app.moon.presentation.myStore.ItemWorkingHours2
import grand.app.moon.presentation.stats.models.ItemStoreStats
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import retrofit2.http.Query
import javax.inject.Inject

class ShopRemoteDataSource @Inject constructor(private val apiService: ShopServices) : BaseRemoteDataSource() {

	suspend fun getMyCategories(page: Int) = safeApiCall2 {
		apiService.getMyCategories(page)
	}
	suspend fun deleteCategory(id: Int) = safeApiCall {
		apiService.deleteCategory(id)
	}
	suspend fun createCategory(name: String) = safeApiCall {
		apiService.createCategory(name)
	}
	suspend fun updateCategory(id: Int, name: String) = safeApiCall {
		apiService.updateCategory(id, name)
	}

	suspend fun getMySubCategories(page: Int) = safeApiCall2 {
		apiService.getMySubCategories(page)
	}
	suspend fun getMySubCategoriesWithParentId(parentId: Int, page: Int) = safeApiCall2 {
		apiService.getMySubCategoriesWithParentId(parentId, page)
	}
	suspend fun deleteSubCategory(id: Int) = safeApiCall {
		apiService.deleteSubCategory(id)
	}
	suspend fun createSubCategory(name: String, parentId: Int) = safeApiCall {
		apiService.createSubCategory(name, parentId)
	}
	suspend fun updateSubCategory(id: Int, name: String, parentId: Int) = safeApiCall {
		apiService.updateSubCategory(id, name, parentId)
	}

	suspend fun getWorkingHours() = safeApiCall {
		apiService.getWorkingHours()
	}

	suspend fun saveWorkingHours(list: List<ItemWorkingHours2>) = safeApiCall {
		val map = mutableMapOf<String, RequestBody>()

		for ((index, item) in list.withIndex()) {
			map["working_hours[$index][from]"] = item.from.toRequestBody()
			map["working_hours[$index][to]"] = item.to.toRequestBody()
			map["working_hours[$index][status]"] = item.enabled.toString().toRequestBody()
		}

		apiService.saveWorkingHours(map)
	}

	suspend fun getSocialMedia() = safeApiCall {
		apiService.getSocialMedia()
	}

	suspend fun saveSocialMedia(list: List<ResponseStoreSocialMedia>) = safeApiCall {
		val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()

		var index = 0
		for (item in list) {
			if (item.linkUrl.isNullOrEmpty().not()) {
				map["social_media_links[$index][type]"] = item.typeOfLink?.apiType.orEmpty().toRequestBody()
				map["social_media_links[${index++}][link]"] = item.linkUrl.orEmpty().toRequestBody()
			}
		}

		apiService.saveSocialMedia(map)
	}

	suspend fun getClientsReviews(
		query: String?,
		from: String?,
		to: String?,
		page: Int
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		if (!query.isNullOrEmpty()) map["search"] = query
		if (!from.isNullOrEmpty()) map["from"] = from
		if (!to.isNullOrEmpty()) map["to"] = to

		apiService.getClientsReviews(page, map)
	}

	suspend fun getShopClientsReviews(
		query: String?,
		from: String?,
		to: String?,
		page: Int
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		if (!query.isNullOrEmpty()) map["search"] = query
		if (!from.isNullOrEmpty()) map["from"] = from
		if (!to.isNullOrEmpty()) map["to"] = to

		apiService.getShopClientsReviews(page, map)
	}

	suspend fun getClientsReviewsForAdv(advertisementId: Int, page: Int) = safeApiCall2 {
		apiService.getClientsReviewsForAdv(advertisementId, page)
	}

	suspend fun getExploresRemainingCount(): Resource<BaseResponse<Int?>> = safeApiCall {
		apiService.getExplores(1, emptyMap()).map {
			it?.exploresRestCount.orZero()
		}.toBaseResponse()
	}

	suspend fun getExplores(
		from: String?,
		to: String?,
		page: Int
	): MAResult.Immediate<MABaseResponse<MABasePaging<ItemExploreInShopInfo>>> = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		if (!from.isNullOrEmpty()) map["from"] = from
		if (!to.isNullOrEmpty()) map["to"] = to

		//    fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>

		apiService.getExplores(page, map).map {
			it?.explores?.map { item ->
				item.copy(
					exploresRestCount = it.exploresRestCount
				)
			}
		}
	}

	suspend fun getStoriesRemainingCount(): Resource<BaseResponse<Int?>> = safeApiCall {
		apiService.getStories(1, emptyMap()).map {
			it?.storiesRestCount.orZero()
		}.toBaseResponse()
	}

	suspend fun getStories(
		from: String?,
		to: String?,
		storyType: StoryType?,
		page: Int
	): MAResult.Immediate<MABaseResponse<MABasePaging<ItemStoryInShopInfo>>> = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		if (!from.isNullOrEmpty()) map["from"] = from
		if (!to.isNullOrEmpty()) map["to"] = to
		if (storyType != null) map["type"] = storyType.apiValue.toStringOrEmpty()

		apiService.getStories(page, map).map {
			it?.stories?.map { item ->
				item.copy(
					storiesRestCount = it.storiesRestCount
				)
			}
		}
	}

	suspend fun addExplore(files: List<MultipartBody.Part>) = safeApiCall { apiService.addExplore(files) }

	suspend fun deleteExplore(id: Int) = safeApiCall { apiService.deleteExplore(id) }

	suspend fun deleteStory(id: Int) = safeApiCall { apiService.deleteStory(id) }

	suspend fun addReviewForAdv(
		advertisementId: Int,
		rate: Int?,
		review: String
	) = safeApiCall {
		val map = mutableMapOf<String, String>()
		if (rate != null) map["rate"] = rate.toString()

		apiService.addReviewForAdv(advertisementId, review, map)
	}

	suspend fun addStory(
		file: MultipartBody.Part,
		storyLink: StoryLink,
		storyType: StoryType,
		name: String?,
		coverImage: MultipartBody.Part?,
	) = safeApiCall {
		val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()
		if (storyType == StoryType.HIGHLIGHT) {
			map["highlight_name"] = name.orEmpty().toRequestBody()
		}

		apiService.addStory(
			listOfNotNull(file, coverImage),
			storyLink.apiValue,
			storyType.apiValue,
			map
		)
	}

	suspend fun getMyAdvStats(
		advId: Int,
		type: ItemStatsInAdvDetails.Type,
	) = safeApiCall {
		apiService.getMyAdvStats(advId, type.apiValue)
	}

	suspend fun getMyAdvStatsUsers(
		advId: Int,
		type: ItemStatsInAdvDetails.Type,
		page: Int,
		query: String?,
		from: String?,
		to: String?,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		if (!query.isNullOrEmpty()) map["name"] = query
		if (!from.isNullOrEmpty()) map["from"] = from
		if (!to.isNullOrEmpty()) map["to"] = to

		apiService.getMyAdvStatsUsers(advId, type.apiValue, page, map)
	}

	suspend fun getReviewsForAdv(advertisementId: Int, page: Int) = safeApiCall2 {
		apiService.getReviewsForAdv(advertisementId, page)
	}

	suspend fun getHomeExplores(page: Int) = safeApiCall2 { apiService.getHomeExplores(page) }

	suspend fun getStoreStats(
		from: String?,
		to: String?,
	) = safeApiCall {
		val map = mutableMapOf<String, String>()

		if (!from.isNullOrEmpty()) map["from"] = from
		if (!to.isNullOrEmpty()) map["to"] = to

		apiService.getStoreStats(map)
	}

	suspend fun getGeneralStatsForStoreStats(type: ItemStoreStats.Type) = safeApiCall {
		apiService.getGeneralStatsForStoreStats(type.apiValue)
	}

	suspend fun getGeneralStatsForStoreStatsUsers(
		type: ItemStoreStats.Type,
		page: Int,
		query: String?,
		from: String?,
		to: String?,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		if (!query.isNullOrEmpty()) map["name"] = query
		if (!from.isNullOrEmpty()) map["from"] = from
		if (!to.isNullOrEmpty()) map["to"] = to

		apiService.getGeneralStatsForStoreStatsUsers(
			type.apiValue,
			page,
			map
		)
	}

	suspend fun getSearchResults(
		search: String,
		type: TypeSearchResult,
		page: Int
	) = safeApiCall2 {
		apiService.getSearchResults(search, type.apiValue, page)
	}

	suspend fun getComplainsAndSuggestionsTypes() = safeApiCall {
		apiService.getSettingsTypes(22)
	}
	suspend fun getContactUsTypes() = safeApiCall {
		apiService.getSettingsTypes(2)
	}

	suspend fun setComplainsAndSuggestionsSettings(
		name: String,
		reasonId: Int,
		message: String,
		phone: String,
		image: MultipartBody.Part?,
	) = safeApiCall {
		apiService.setSettings(
			name.toRequestBody(),
			reasonId,
			message.toRequestBody(),
			phone.toRequestBody(),
			listOfNotNull(image),
			3
		)
	}
	suspend fun setContactUsSettings(
		name: String,
		reasonId: Int,
		message: String,
		phone: String,
		image: MultipartBody.Part?,
	) = safeApiCall {
		apiService.setSettings(
			name.toRequestBody(),
			reasonId,
			message.toRequestBody(),
			phone.toRequestBody(),
			listOfNotNull(image),
			1
		)
	}

	suspend fun getContactUsData() = safeApiCall {
		apiService.getContactUsData()
	}

	suspend fun getAppSocialMedia() = safeApiCall {
		apiService.getAppSocialMedia()
	}

	suspend fun getAllStories(page: Int) = safeApiCall2 {
		apiService.getAllStories(page)
	}

}
