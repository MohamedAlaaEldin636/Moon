package grand.app.moon.data.shop

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.ads.DynamicFilterProperty
import grand.app.moon.domain.shop.*
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.*
import grand.app.moon.presentation.home.AllStoresFragment
import grand.app.moon.presentation.home.FilterAllFragment
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragment
import grand.app.moon.presentation.home.models.*
import grand.app.moon.presentation.map.MapOfDataFragment
import grand.app.moon.presentation.myAds.model.ItemStatsInAdvDetails
import grand.app.moon.presentation.myStore.ItemWorkingHours2
import grand.app.moon.presentation.stats.models.ItemStoreStats
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

		if (map.isNotEmpty()) apiService.saveSocialMedia(map) else apiService.saveSocialMediaEmpty()
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

	suspend fun addExplore(files: List<MultipartBody.Part>): Resource<BaseResponse<Any?>> = safeApiCall {
		suspendCoroutine {
			apiService.addExplore(files).enqueue(object : retrofit2.Callback<BaseResponse<Any?>> {
				override fun onResponse(
					call: Call<BaseResponse<Any?>>,
					response: Response<BaseResponse<Any?>>
				) {
					//MyLogger.e("feowifjewohiiiiiiiiiii BEFOOOOOOOOOOOOOOOOOOOOORE")
					kotlin.runCatching {
						it.resume(response.body() ?: BaseResponse(null, "dd", 200))
					}
				}

				override fun onFailure(call: Call<BaseResponse<Any?>>, t: Throwable) {
					//MyLogger.e("feowifjewohiiiiiiiiiii EEEEEEEEEEEEEEEEEEEEEE")
					kotlin.runCatching {
						it.resume(BaseResponse(null, "dd", 200))
					}
				}
			})
		}
	}

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

	suspend fun addReviewForStore(
		storeId: Int,
		rate: Int?,
		review: String
	) = safeApiCall {
		val map = mutableMapOf<String, String>()
		if (rate != null) map["rate"] = rate.toString()

		apiService.addReviewForStore(storeId, review, map)
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

		MyLogger.e("kdjasdjasl $map $coverImage $file")

		apiService.addStory(
			storyLink.apiValue.toString().toRequestBody(),
			storyType.apiValue.toString().toRequestBody(),
			listOfNotNull(file, coverImage),
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

	suspend fun getReviewsForStore(storeId: Int, page: Int) = safeApiCall2 {
		apiService.getReviewsForStore(storeId, page)
	}

	suspend fun getHomeExplores(page: Int) = safeApiCall2 { apiService.getHomeExplores(page) }

	suspend fun getHomeExplores2(page: Int) = safeApiCall2 {
		apiService.getHomeExplores2(page)
	}

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

	suspend fun getStatusUserHistoryOfExploreOrStoryInShopInfo(
		type: SimpleUserListOfInteractionsFragment.Type,
		storyOrExploreId: Int,
		userId: Int,
	) = safeApiCall2 {
		val isStoryNotExplore = type == SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_LIKES
			|| type == SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_VIEWS
			|| type == SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_SHARES

		apiService.getStatusUserHistoryOfExploreOrStoryInShopInfo(
			if (isStoryNotExplore) "stories" else "explores",
			when (type) {
				SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_VIEWS -> "views"
				SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_STORY_SHARES,
				SimpleUserListOfInteractionsFragment.Type.SHOP_INFO_EXPLORE_SHARES -> "shares"
				else -> "NONE"
			},
			storyOrExploreId,
			userId
		)
	}

	suspend fun getStatusUsersHistory(
		type: String,
		userId: Int,
		advId: Int?,
		storeId: Int?,
		page: Int
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()
		if (advId != null) {
			map["advertisement_id"] = advId.toString()
		}
		if (storeId != null) {
			map["store_id"] = storeId.toString()
		}

		apiService.getStatusUsersHistory(
			type,
			if (advId == null) "store" else "advertisement",
			userId,
			page,
			map
		)
	}

	suspend fun followStore(storeId: Int) = safeApiCall { apiService.followStore(storeId) }

	suspend fun deleteAccountPermanently() = safeApiCall { apiService.deleteAccountPermanently() }

	suspend fun setExploreActionInteractive(
		exploreId: Int,
		type: Int,
	) = safeApiCall {
		apiService.setExploreActionInteractive(exploreId, type)
	}

	suspend fun getSimpleUsersOfExploreLikes(id: Int, page: Int) = safeApiCall2 {
		apiService.getSimpleUsersOfExploreLikes(id, page)
	}

	suspend fun getAllAppCategoriesWithSubcategoriesAndBrands() = safeApiCall {
		apiService.getAllAppCategoriesWithSubcategoriesAndBrands()
	}

	suspend fun storyInteractions(
		storyId: Int,
		interaction: Interaction.Story
	) = safeApiCall {
		apiService.storyInteractions(storyId, interaction.apiValue)
	}

	suspend fun getMapDataForStore(
		categoryId: Int?,
		subCategoryId: Int?,
		propertyId: Int?,
	) = safeApiCall {
		val map = buildMap {
			if (categoryId != null && categoryId != -1) this["category_id"] = categoryId.toString()
			if (subCategoryId != null && subCategoryId != -1) this["sub_category_id"] = subCategoryId.toString()
			if (propertyId != null && propertyId != -1) this["property_id"] = propertyId.toString()
		}

		apiService.getMapDataForStore(MapOfDataFragment.Type.STORE.apiValue, map)
	}
	suspend fun getMapDataForAdv(
		categoryId: Int?,
		subCategoryId: Int?,
		propertyId: Int?,
	) = safeApiCall {
		val map = buildMap {
			if (categoryId != null && categoryId != -1) this["category_id"] = categoryId.toString()
			if (subCategoryId != null && subCategoryId != -1) this["sub_category_id"] = subCategoryId.toString()
			if (propertyId != null && propertyId != -1) this["property_id"] = propertyId.toString()
		}

		apiService.getMapDataForAdv(MapOfDataFragment.Type.ADVERTISEMENT.apiValue, map)
	}

	suspend fun favOrUnFavAdv(advId: Int) = safeApiCall {
		apiService.favOrUnFavAdv(advId)
	}

	suspend fun shareAdv(advId: Int) = safeApiCall {
		apiService.shareAdv(advId)
	}

	suspend fun getDynamicFilterProperties(categoryId: Int, subCategoryId: Int?) = safeApiCall {
		val map = mutableMapOf<String, String>()
		if (subCategoryId != null) map["sub_category_id"] = subCategoryId.toString()

		apiService.getDynamicFilterProperties(categoryId, map)
	}

	suspend fun getFilterResults(
		page: Int,
		filter: FilterAllFragment.Filter,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		filter.search.ifNotNullNorEmpty { map["search"] = it }
		filter.categoryId.ifNotNull { map["category_id"] = it.toString() }
		filter.subCategoryId.ifNotNull { map["sub_category_id"] = it.toString() }
		filter.brandId.ifNotNull { map["brand_id"] = it.toString() }
		filter.minPrice.ifNotNull { map["min_price"] = it.toString() }
		filter.maxPrice.ifNotNull { map["max_price"] = it.toString() }
		filter.cityId.ifNotNull { map["city_ids[0]"] = it.toString() }
		filter.areasIds?.forEachIndexed { index, id ->
			map["area_ids[$index]"] = id.toString()
		}
		var index = 0
		filter.properties.forEach { item ->
			val (id, from, to) = when (item) {
				is DynamicFilterProperty.Checked -> if (item.isSelected) {
					item.id to null triple null
				}else {
					return@forEach
				}
				is DynamicFilterProperty.RangedText -> if (item.from.isNotEmpty() && item.to.isNotEmpty()) {
					item.id to item.from triple item.to
				}else {
					return@forEach
				}
				is DynamicFilterProperty.Selection -> {
					item.selectedData.forEach { (id, _) ->
						id.ifNotNull { map["properties[${index++}][id]"] = it.toString() }
					}

					return@forEach
				}
				is DynamicFilterProperty.Text -> if (item.value.isNotEmpty()) {
					item.id to item.value triple item.value
				}else {
					return@forEach
				}
			}

			map["properties[$index][id]"] = id.toString()
			from.ifNotNullNorEmpty { map["properties[$index][from]"] = from.toString() }
			from.ifNotNullNorEmpty { map["properties[$index][to]"] = to.toString() }
			index++
		}
		filter.sortBy.ifNotNull { map["order_by"] = it.apiValue.toString() }
		filter.adType.ifNotNull { map["other_options"] = it.apiValue.toString() }
		filter.rating.ifNotNull {
			map["min_rate"] = it.toString()
			map["max_rate"] = 5.toString()
		}
		filter.adSpecificType.ifNotNull { map["type"] = it.apiValue.toString() }

		apiService.getFilterResults(page, map)
	}

	suspend fun getAllStores(
		page: Int,
		filter: AllStoresFragment.Filter,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		filter.search.ifNotNullNorEmpty { map["search"] = it }
		filter.categoryId.ifNotNull { map["category_ids[0]"] = it.toString() }
		filter.subCategoryId.ifNotNull { map["sub_category_ids[0]"] = it.toString() }
		filter.cityId.ifNotNull { map["city_ids[0]"] = it.toString() }
		filter.areasIds?.forEachIndexed { index, id ->
			map["area_ids[$index]"] = id.toString()
		}
		filter.sortBy.ifNotNull { map["order_by"] = it.apiValue.toString() }
		filter.rating.ifNotNull {
			map["min_rate"] = it.toString()
			map["max_rate"] = 5.toString()
		}

		apiService.getAllStores(page, map)
	}

	suspend fun getCategoryDetails(categoryId: Int) = safeApiCall {
		apiService.getCategoryDetails(categoryId)
	}

	suspend fun getCategoryStories(categoryId: Int) = safeApiCall {
		apiService.getCategoryStories(categoryId)
	}

	suspend fun getAllAds(
		page: Int,
		filter: FilterAllFragment.Filter,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		filter.search.ifNotNullNorEmpty { map["search"] = it }
		filter.categoryId.ifNotNull { map["category_id"] = it.toString() }
		filter.subCategoryId.ifNotNull { map["sub_category_id"] = it.toString() }
		filter.brandId.ifNotNull { map["brand_id"] = it.toString() }
		filter.minPrice.ifNotNull { map["min_price"] = it.toString() }
		filter.maxPrice.ifNotNull { map["max_price"] = it.toString() }
		filter.cityId.ifNotNull { map["city_ids[0]"] = it.toString() }
		filter.areasIds?.forEachIndexed { index, id ->
			map["area_ids[$index]"] = id.toString()
		}
		var index = 0
		filter.properties.forEach { item ->
			val (id, from, to) = when (item) {
				is DynamicFilterProperty.Checked -> if (item.isSelected) {
					item.id to null triple null
				}else {
					return@forEach
				}
				is DynamicFilterProperty.RangedText -> if (item.from.isNotEmpty() && item.to.isNotEmpty()) {
					item.id to item.from triple item.to
				}else {
					return@forEach
				}
				is DynamicFilterProperty.Selection -> {
					item.selectedData.forEach { (id, _) ->
						id.ifNotNull { map["properties[${index++}][id]"] = it.toString() }
					}

					return@forEach
				}
				is DynamicFilterProperty.Text -> if (item.value.isNotEmpty()) {
					item.id to item.value triple item.value
				}else {
					return@forEach
				}
			}

			map["properties[$index][id]"] = id.toString()
			from.ifNotNullNorEmpty { map["properties[$index][from]"] = from.toString() }
			from.ifNotNullNorEmpty { map["properties[$index][to]"] = to.toString() }
			index++
		}
		filter.sortBy.ifNotNull { map["order_by"] = it.apiValue.toString() }
		filter.adType.ifNotNull { map["other_options"] = it.apiValue.toString() }
		filter.rating.ifNotNull {
			map["min_rate"] = it.toString()
			map["max_rate"] = 5.toString()
		}
		filter.adSpecificType.ifNotNull { map["type"] = it.apiValue.toString() }

		apiService.getAllAds(page, map)
	}

	suspend fun getAllAdsAsResponseBody(
		page: Int,
		filter: FilterAllFragment.Filter,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		filter.search.ifNotNullNorEmpty { map["search"] = it }
		filter.categoryId.ifNotNull { map["category_id"] = it.toString() }
		filter.subCategoryId.ifNotNull { map["sub_category_id"] = it.toString() }
		filter.brandId.ifNotNull { map["brand_id"] = it.toString() }
		filter.minPrice.ifNotNull { map["min_price"] = it.toString() }
		filter.maxPrice.ifNotNull { map["max_price"] = it.toString() }
		filter.cityId.ifNotNull { map["city_ids[0]"] = it.toString() }
		filter.areasIds?.forEachIndexed { index, id ->
			map["area_ids[$index]"] = id.toString()
		}
		var index = 0
		filter.properties.forEach { item ->
			val (id, from, to) = when (item) {
				is DynamicFilterProperty.Checked -> if (item.isSelected) {
					item.id to null triple null
				}else {
					return@forEach
				}
				is DynamicFilterProperty.RangedText -> if (item.from.isNotEmpty() && item.to.isNotEmpty()) {
					item.id to item.from triple item.to
				}else {
					return@forEach
				}
				is DynamicFilterProperty.Selection -> {
					item.selectedData.forEach { (id, _) ->
						id.ifNotNull { map["properties[${index++}][id]"] = it.toString() }
					}

					return@forEach
				}
				is DynamicFilterProperty.Text -> if (item.value.isNotEmpty()) {
					item.id to item.value triple item.value
				}else {
					return@forEach
				}
			}

			map["properties[$index][id]"] = id.toString()
			from.ifNotNullNorEmpty { map["properties[$index][from]"] = from.toString() }
			from.ifNotNullNorEmpty { map["properties[$index][to]"] = to.toString() }
			index++
		}
		filter.sortBy.ifNotNull { map["order_by"] = it.apiValue.toString() }
		filter.adType.ifNotNull { map["other_options"] = it.apiValue.toString() }
		filter.rating.ifNotNull {
			map["min_rate"] = it.toString()
			map["max_rate"] = 5.toString()
		}
		filter.adSpecificType.ifNotNull { map["type"] = it.apiValue.toString() }

		val json = kotlin.runCatching {
			apiService.getAllAdsAsResponseBody(page, map)
				.string()
		}.getOrElse {
			null
		}

		if (filter.categoryId != null) {
			json.fromJsonInlinedOrNull<MABaseResponse<ResponseAllAdsOfCategory>>()
				.toSpecialResponseOfAllAds1().toPaging()
		}else {
			json.fromJsonInlinedOrNull<MABaseResponse<MABasePaging<ItemAdvertisementInResponseHome>>>()
				.toSpecialResponseOfAllAds2().toPaging()
		} ?: MABaseResponse(null, "", 401)
	}

	suspend fun getAllAdsOfCategory(
		page: Int,
		filter: FilterAllFragment.Filter,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()

		filter.search.ifNotNullNorEmpty { map["search"] = it }
		filter.categoryId.ifNotNull { map["category_id"] = it.toString() }
		filter.subCategoryId.ifNotNull { map["sub_category_id"] = it.toString() }
		filter.brandId.ifNotNull { map["brand_id"] = it.toString() }
		filter.minPrice.ifNotNull { map["min_price"] = it.toString() }
		filter.maxPrice.ifNotNull { map["max_price"] = it.toString() }
		filter.cityId.ifNotNull { map["city_ids[0]"] = it.toString() }
		filter.areasIds?.forEachIndexed { index, id ->
			map["area_ids[$index]"] = id.toString()
		}
		var index = 0
		filter.properties.forEach { item ->
			val (id, from, to) = when (item) {
				is DynamicFilterProperty.Checked -> if (item.isSelected) {
					item.id to null triple null
				}else {
					return@forEach
				}
				is DynamicFilterProperty.RangedText -> if (item.from.isNotEmpty() && item.to.isNotEmpty()) {
					item.id to item.from triple item.to
				}else {
					return@forEach
				}
				is DynamicFilterProperty.Selection -> {
					item.selectedData.forEach { (id, _) ->
						id.ifNotNull { map["properties[${index++}][id]"] = it.toString() }
					}

					return@forEach
				}
				is DynamicFilterProperty.Text -> if (item.value.isNotEmpty()) {
					item.id to item.value triple item.value
				}else {
					return@forEach
				}
			}

			map["properties[$index][id]"] = id.toString()
			from.ifNotNullNorEmpty { map["properties[$index][from]"] = from.toString() }
			from.ifNotNullNorEmpty { map["properties[$index][to]"] = to.toString() }
			index++
		}
		filter.sortBy.ifNotNull { map["order_by"] = it.apiValue.toString() }
		filter.adType.ifNotNull { map["other_options"] = it.apiValue.toString() }
		filter.rating.ifNotNull {
			map["min_rate"] = it.toString()
			map["max_rate"] = 5.toString()
		}
		filter.adSpecificType.ifNotNull { map["type"] = it.apiValue.toString() }

		apiService.getAllAdsOfCategory(
			page,
			map
		)
	}

	suspend fun getAdvDetails(id: Int, fromSearchNotView: Boolean) = safeApiCall {
		apiService.getAdvDetails(id, if (fromSearchNotView) 5 else 2)
	}

	suspend fun getStoreDetails(id: Int, fromSearchNotView: Boolean) = safeApiCall {
		apiService.getStoreDetails(id, if (fromSearchNotView) 4 else 3)
	}

	suspend fun getAdvReportingReason() = safeApiCall {
		apiService.getAdvReportingReason()
	}

	suspend fun reportAdv(
		advertisementId: Int,
		reasonId: Int,
	) = safeApiCall {
		apiService.reportAdv(advertisementId, reasonId)
	}

	suspend fun shareStore(storeId: Int) = safeApiCall {
		apiService.shareStore(storeId)
	}

	suspend fun getStoreViews(id: Int) = safeApiCall2 {
		apiService.getStoreViews(id)
	}

	suspend fun getStoreFollowers(id: Int) = safeApiCall2 {
		apiService.getStoreFollowers(id)
	}

	suspend fun getStoreReportingReasons() = safeApiCall {
		apiService.getStoreReportingReasons()
	}

	suspend fun getStoreBlockingReasons() = safeApiCall {
		apiService.getStoreBlockingReasons()
	}

	suspend fun reportStore(storeId: Int, reasonId: Int) = safeApiCall {
		apiService.reportOrBlockStore(storeId, reasonId, 1)
	}

	suspend fun blockStore(storeId: Int, reasonId: Int) = safeApiCall {
		apiService.reportOrBlockStore(storeId, reasonId, 2)
	}

	suspend fun getChatAgent() = safeApiCall {
		apiService.getChatAgent()
	}

	suspend fun getFollowedStores(
		categoryId: Int?,
		page: Int,
	) = safeApiCall2 {
		val map = mutableMapOf<String, String>()
		categoryId.ifNotNull { map["category_id"] = it.toString() }

		apiService.getFollowedStores(page, map)
	}
	
	suspend fun getStoryViews(id: Int, page: Int) = safeApiCall2 { apiService.getStoryViews(id, page) }
	suspend fun getStoryLikes(id: Int, page: Int) = safeApiCall2 { apiService.getStoryLikes(id, page) }
	suspend fun getStoryShares(id: Int, page: Int) = safeApiCall2 { apiService.getStoryShares(id, page) }
	suspend fun getExploreComments(id: Int, page: Int) = safeApiCall2 { apiService.getExploreComments(id, page) }
	suspend fun getExploreLikes(id: Int, page: Int) = safeApiCall2 { apiService.getExploreLikes(id, page) }
	suspend fun getExploreShares(id: Int, page: Int) = safeApiCall2 { apiService.getExploreShares(id, page) }

	suspend fun getAnnouncement() = safeApiCall2 { apiService.getAnnouncement() }

	suspend fun interactionForAdv(advId: Int, type: TypeAdOrStoreInteraction) = safeApiCall {
		apiService.interactionForAd(advId, type.apiValue)
	}

	suspend fun interactionForStore(storeId: Int, type: TypeAdOrStoreInteraction) = safeApiCall {
		apiService.interactionForStore(storeId, type.apiValue)
	}

	suspend fun getExploreDetails(id: Int) = safeApiCall { apiService.getExploreDetails(id) }

	suspend fun sendCode(countryCode: String, phone: String, isForAdsNotWhatsApp: Boolean) = safeApiCall {
		apiService.sendCode(
			countryCode,
			phone,
			if (isForAdsNotWhatsApp) "4" else "3"
		)
	}

	suspend fun getRestOfDaysInMyPackageIfHaveOneOrZeroInstead() = safeApiCall {
		apiService.getRestOfDaysInMyPackageIfHaveOneOrZeroInstead()
	}

	suspend fun getWhatsappHistory(page: Int) = safeApiCall2 { apiService.getWhatsappHistory(page) }

	suspend fun getLastViewedAds(page: Int) = safeApiCall2 { apiService.getLastViewedAds(page) }
	suspend fun getLastSearchedAds(page: Int) = safeApiCall2 { apiService.getLastSearchedAds(page) }
	suspend fun getFavByOtherUsersAds(page: Int) = safeApiCall2 { apiService.getFavByOtherUsersAds(page) }

	enum class TypeAdOrStoreInteraction(val apiValue: Int) {
		CALL(6), WHATSAPP(7), CHAT(8)
	}

}
