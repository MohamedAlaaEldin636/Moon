package grand.app.moon.presentation.map.model

import com.google.gson.annotations.SerializedName
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.myStore.model.ResponseCity

fun ResponseMapDataForStore.toResponseMapData(): ResponseMapData {
	return ResponseMapData(
		id, name, image, stories, category, latitude, longitude,
	)
}
fun ResponseMapDataForAdv.toResponseMapData(): ResponseMapData {
	return ResponseMapData(
		null, null, null, null, category?.let { listOf(it) }, latitude, longitude, title, isFavorite,
		premium, favoriteCount, viewsCount, averageRate, createdAt, country, city, store, price, negotiable
	)
}

data class ResponseMapDataForStore(
	var id: Int?,
	var name: String?,
	var image: String?,
	var category: List<ItemCategoryInResponseMapData>?,
	var stories: List<ResponseStory.Story>?,
	var latitude: Double?,
	var longitude: Double?,
)

data class ResponseMapDataForAdv(
	var category: ItemCategoryInResponseMapData?,
	var title: String?,
	@SerializedName("is_favorite") var isFavorite: Boolean?,
	var premium: Int,
	@SerializedName("favorite_count") var favoriteCount: Int?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("average_rate") var averageRate: Float?,
	@SerializedName("created_at") var createdAt: String?,
	var country: ItemAdvertisementInResponseHome.Country?, // name
	var city: ItemAdvertisementInResponseHome.City?, // name
	var store: ItemAdvertisementInResponseHome.Store?, // stories, id, image, nicname
	var price: Float?,
	@SerializedName("is_negotiable") var negotiable: Int?,
	var latitude: Double?,
	var longitude: Double?,
)

/**
 * @param name same as [title] but [name] is if shop
 */
data class ResponseMapData(
	// For store item.
	var id: Int? = null,
	var name: String? = null,
	var image: String? = null,
	var stories: List<ResponseStory.Story>? = null,

	// Both but in adv only have 1 item.
	var categories: List<ItemCategoryInResponseMapData>? = null,
	var latitude: Double? = null,
	var longitude: Double? = null,

	// For advertisement item.
	var title: String? = null,
	var isFavorite: Boolean? = null,
	var premium: Int? = null,
	var favoriteCount: Int? = null,
	var viewsCount: Int? = null,
	var averageRate: Float? = null,
	var createdAt: String? = null,
	var country: ItemAdvertisementInResponseHome.Country? = null, // name
	var city: ItemAdvertisementInResponseHome.City? = null, // name
	var store: ItemAdvertisementInResponseHome.Store? = null, // stories, id, image, nicname
	var price: Float? = null,
	var negotiable: Int? = null,

	var phone: String? = null,
	var nickname: String? = null,
)

data class ItemCategoryInResponseMapData(
	var id: Int?,
	var name: String?,
	var image: String?,
)

/*data class ItemCountryInResponseMapData(
	var id: Int?,
	var name: String?,
	var currency: String?,
	@SerializedName("country_code") var countryCode: String?,
	@SerializedName("iso_code") var isoCode: String?,
	var image: String?,
	var cities: List<ResponseCity>?,
)*/
