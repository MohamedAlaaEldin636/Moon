package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName

data class ItemHomeRV(
	val type: Type,
	val name: String?,
	val count: Int? = null,
	val dynamicCategoriesAdsId: Int? = null,
) {
	enum class Type(val apiValue: String) {
		STORIES("stories"),
		CATEGORIES("categories"),
		MOST_RATED_STORIES("most-rated-stores"),
		FOLLOWING_STORIES("followings-stores"),
		SUGGESTED_ADS("suggested-ads"),
		MOST_POPULAR_ADS("most-popular-ads"),
		DYNAMIC_CATEGORIES_ADS("category-ads"),
	}
}

sealed interface ItemInResponseHome

data class ItemStoreInResponseHome(
	var id: Int?,
	var image: String?,
	var name: String?,
	var nickname: String?,
	@SerializedName("average_rate") var averageRate: Float?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("advertisements_count") var advertisementsCount: Int?,
	@SerializedName("is_following") var isFollowing: Boolean?,
	var premium: Int?,
) : ItemInResponseHome {
	val isPremium get() = premium == 1
}

data class ItemAdvertisementInResponseHome(
	var id: Int?,
	@SerializedName("is_favorite") var isFavorite: Boolean?,
	var premium: Int?,
	var image: String?,
	@SerializedName("favorite_count") var favoriteCount: Int?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("average_rate") var averageRate: Float?,
	var title: String?,
	@SerializedName("created_at") var createdAt: String?,
	var country: Country?, // name
	var city: City?, // name
	var store: Store?, // stories, id, image, nicname
	var price: Float?,
	@SerializedName("is_negotiable") var negotiable: Int?,
	var phone: String?,
) : ItemInResponseHome {

	val isNegotiable get() = negotiable == 1

	val isPremium get() = premium == 1

	data class Country(
		var id: Int?,
		var name: String?,
		var currency: String?,
		@SerializedName("country_code") var countryCode: String?,
	)

	data class City(
		var id: Int?,
		var name: String?,
	)

	data class Store(
		var id: Int?,
		var image: String?,
		var name: String?,
		var nickname: String?,
		var stories: List<ResponseStory.Story>?,
		var phone: String?,
		var createdAt: String?,
		var country: Country?,
	)
}

data class ResponseHome(
	@SerializedName("most-rated-stores") var mostRatedStores: List<ItemStoreInResponseHome>?,
	@SerializedName("followings-stores") var followingsStores: List<ItemStoreInResponseHome>?,
	@SerializedName("suggested-ads") var suggestedAds: List<ItemAdvertisementInResponseHome>?,
	@SerializedName("most-popular-ads") var mostPopularAds: List<ItemAdvertisementInResponseHome>?,
	@SerializedName("category-ads") var dynamicCategoriesAds: List<Category>?,
) {

	data class Category(
		var id: Int?,
		var name: String?,
		var image: String?,
		@SerializedName("ads_count") var adsCount: Int?,
		@SerializedName("order_by_no") var orderByNo: Int?,
		var advertisements: List<ItemAdvertisementInResponseHome>?,
	)

}
