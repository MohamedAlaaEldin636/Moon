package grand.app.moon.data.shop

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.shop.ItemExploreInShopInfo
import grand.app.moon.domain.shop.ItemStoryInShopInfo
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.domain.shop.StoryType
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.toStringOrEmpty
import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.helpers.paging.MAResult
import grand.app.moon.helpers.paging.map
import grand.app.moon.presentation.myStore.ItemWorkingHours2
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

}
