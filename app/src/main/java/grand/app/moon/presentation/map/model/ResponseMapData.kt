package grand.app.moon.presentation.map.model

import com.google.gson.annotations.SerializedName
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ResponseStory

fun ResponseMapDataForStore.toResponseMapData(): ResponseMapData {
	return ResponseMapData(
		id, name, image, stories, category, latitude, longitude, phone = phone, nickname = nickname, country = country,
		adsPhone = adsPhone, adsCountryCode = adsCountryCode, whatsappPhone = whatsappPhone, whatsappCountryCode = whatsappCountryCode
	)
}
fun ResponseMapDataForAdv.toResponseMapData(): ResponseMapData {
	return ResponseMapData(
		id, null, image, null, category?.let { listOf(it) }, latitude, longitude, title, isFavorite,
		premium, favoriteCount, viewsCount, averageRate, createdAt, country, city, store, price, negotiable,
		priceBefore = priceBefore, phone = phone,
		adsPhone = adsPhone, adsCountryCode = adsCountryCode, whatsappPhone = whatsappPhone, whatsappCountryCode = whatsappCountryCode
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

	var phone: String? = null,
	var nickname: String? = null,
	var country: ItemAdvertisementInResponseHome.Country? = null,

	@SerializedName("ads_phone") var adsPhone: String? = null,
	@SerializedName("whatsapp_phone") var whatsappPhone: String? = null,
	@SerializedName("ads_country_code") var adsCountryCode: String? = null,
	@SerializedName("whatsapp_country_code") var whatsappCountryCode: String? = null,
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
	@SerializedName("price_before") var priceBefore: Float? = null,
	var phone: String?,
	var id: Int?,
	var image: String?,

	@SerializedName("ads_phone") var adsPhone: String? = null,
	@SerializedName("whatsapp_phone") var whatsappPhone: String? = null,
	@SerializedName("ads_country_code") var adsCountryCode: String? = null,
	@SerializedName("whatsapp_country_code") var whatsappCountryCode: String? = null,
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
	var priceBefore: Float? = null,

	@SerializedName("ads_phone") var adsPhone: String? = null,
	@SerializedName("whatsapp_phone") var whatsappPhone: String? = null,
	//var store: Store? = null,,
	@SerializedName("ads_country_code") var adsCountryCode: String? = null,
	@SerializedName("whatsapp_country_code") var whatsappCountryCode: String? = null,
) {
	val isNegotiable get() = negotiable == 1

	fun getFullWhatsAppPhone(isStoreNotAdv: Boolean) = if (isStoreNotAdv) {
		if (store?.whatsappPhone.isNullOrEmpty()) getFullPhone(true) else {
			store?.fullWhatsAppPhone
		}
	}else {
		if (whatsappPhone.isNullOrEmpty()) getFullPhone(false) else {
			val countryCode = whatsappCountryCode.orEmpty()
			"$countryCode${whatsappPhone.orEmpty()}"
		}
	}
	fun getFullAdsPhone(isStoreNotAdv: Boolean) = if (isStoreNotAdv) {
		if (store?.adsPhone.isNullOrEmpty()) getFullPhone(true) else {
			store?.fullAdsPhone
		}
	}else {
		if (adsPhone.isNullOrEmpty()) getFullPhone(false) else {
			val countryCode = adsCountryCode.orEmpty()
			"$countryCode${adsPhone.orEmpty()}"
		}
	}
	private fun getFullPhone(isStoreNotAdv: Boolean) = if (isStoreNotAdv) {
		if (store?.phone.isNullOrEmpty()) "" else "${store?.country?.countryCode.orEmpty()}${store?.phone.orEmpty()}"
	}else {
		if (phone.isNullOrEmpty()) "" else "${country?.countryCode.orEmpty()}${phone.orEmpty()}"
	}
}

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
