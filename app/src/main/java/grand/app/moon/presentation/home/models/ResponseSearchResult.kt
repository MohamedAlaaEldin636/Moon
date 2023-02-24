package grand.app.moon.presentation.home.models

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import grand.app.moon.R

data class ResponseSearchResult(
	var id: Int?,
	@SerializedName("store_id") var storeId: Int?,
	@SerializedName("is_favorite") var isFavorite: Boolean?,
	var premium: Int,
	var image: String?,
	@SerializedName("favorite_count") var favoriteCount: Int?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("average_rate") var averageRate: Float?,
	var title: String?,
	@SerializedName("created_at") var createdAt: String?,
	var country: ItemAdvertisementInResponseHome.Country?, // name
	var city: ItemAdvertisementInResponseHome.City?, // name
	var store: ItemAdvertisementInResponseHome.Store?, // stories, id, image, nicname
	var price: Float?,
	@SerializedName("price_before") var priceBefore: Float?,
	@SerializedName("is_negotiable") var negotiable: Int?,
	var phone: String?,
	@SerializedName("is_following") var isFollowing: Boolean?,
	var name: String?,
	var nickname: String?,
	@SerializedName("advertisements_count") var advertisementsCount: Int?,
) {
	val isNegotiable get() = negotiable == 1

	val isPremium get() = premium == 1
}

enum class TypeSearchResult(val apiValue: String, @StringRes val stringRes: Int) {
	ADVERTISEMENT("advertisement", R.string.advertisements),
	STORE("store", R.string.stores),
	NICKNAME("nickname", R.string.nickname_2819),
	CATEGORY("category", R.string.departments)
}
