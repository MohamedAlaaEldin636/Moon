package grand.app.moon.presentation.myAds.model

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.extensions.orZero
import java.math.BigDecimal
import java.math.RoundingMode

data class ResponseMyAdvertisements(
	var advertisements: List<ResponseMyAdvDetails>?
)

data class ResponseMyAdvDetails(
	var id: Int?,
	var title: String?,
	var description: String?,
	var latitude: String?,
	var longitude: String?,
	var address: String?,
	var price: Int?,
	var premium: Int?,
	@SerializedName("created_at") var createdAt: String?,
	@SerializedName("date") var dateInMs: Long?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("favorite_count") var favoriteCount: Int?,
	@SerializedName("share_count") var shareCount: Int?,
	@SerializedName("city_id") var cityId: Int?,
	var phone: String?,
	@SerializedName("is_favorite") var isFavorite: Boolean?,
	@SerializedName("average_rate") var averageRate: Float?,
	var image: String?,
	var images: List<ItemImage>?,
	var properties: List<ItemPropertyInAdvDetails>?,
	var switches: List<ItemPropertyInAdvDetails>?,
	var reviews: List<ItemReviewInAdvDetails>?,
	var statistics: List<ItemStatsInAdvDetails>?,
	var country: ItemCountryInAdvDetails?,
	@SerializedName("rest_days") var restDays: Int?,
	@SerializedName("is_negotiable") var negotiable: Int?,
	@SerializedName("share_link") var shareLink: String?,
	var category: ItemCategoryInAdvDetails?,
	@SerializedName("sub_category") var subCategory: ItemSubCategoryInAdvDetails?,
	var city: ItemCityInAdvDetails?,
	var brand: ItemBrandInAdvDetails?,

	@SerializedName("price_before") var priceBeforeDiscount: Int?,
	@SerializedName("store_category_id") var storeCategoryId: Int?,
	@SerializedName("store_sub_category_id") var storeSubCategoryId: Int?,
) {
	val isPremium get() = premium == 1

	val isNegotiable get() = negotiable == 1

	fun makePremium() {
		premium = 1
	}

}

data class ItemBrandInAdvDetails(
	var id: Int?,
	var name: String?,
	var image: String?,
	@SerializedName("ads_count") var adsCount: Int?,
	@SerializedName("order_by_no") var orderByNo: Int?,
)

data class ItemSubCategoryInAdvDetails(
	var id: Int?,
	var name: String?,
	var image: String?,
	@SerializedName("ads_count") var adsCount: Int?,
	@SerializedName("order_by_no") var orderByNo: Int?,
)
/*
"sub_category": {
            "id": 14,
            "name": "مرسيدسمرسيدس",
            "image": "https://sooqmoon.net/storage/categories/1652082742N54KO.webp",
            "ads_count": null,
            "order_by_no": null
        }
 */

data class ItemCityInAdvDetails(
	var id: Int?,
	var name: String?,
	var image: String?,
	var currency: String?,
	@SerializedName("currency_code") var currencyCode: String?,
	@SerializedName("iso_code") var isoCode: String?,
)

data class ItemCategoryInAdvDetails(
	var id: Int?,
	var name: String?,
	var image: String?,
	@SerializedName("ads_count") var adsCount: Int?,
	@SerializedName("order_by_no") var orderByNo: Int?,
)

data class ItemCountryInAdvDetails(
	var id: Int?,
	var name: String?,
	var currency: String?,
	@SerializedName("country_code") var countryCode: String?,
	@SerializedName("iso_code") var isoCode: String?,
	var image: String?,
)

data class ItemImage(
	var id: Int?,
	var image: String?,
)

/** 1 -> Multi-Selection, 3 -> Text, else / null / -> Boolean */
data class ItemPropertyInAdvDetails(
	var id: Int?,
	var name: String?,
	@SerializedName("type") var intValueOfType: Int?,
	@SerializedName("is_range") var isRange: Int?,
	var image: String?,
	var text: String?,
	var min: Int?,
	var max: Int?,
	var parent: ItemPropertyInAdvDetails?
) {
	val type get() = Type.values().firstOrNull { it.apiValue == intValueOfType } ?: Type.BOOLEAN

	enum class Type(val apiValue: Int?) {
		SINGLE_SELECTION_OF_MULTIPLE_VALUES(1),
		TEXT(3),
		BOOLEAN(null)
	}
}

data class ItemReviewInAdvDetails(
	var user: ItemUserInReviewsInAdvDetails?,
	var rate: Int?,
	var review: String?,
	var date: String?,
)

data class ItemUserInReviewsInAdvDetails(
	var id: Int?,
	var verified: Int?,
	@SerializedName("is_shop") var isShop: Boolean?,
	var name: String?,
	var email: String?,
	var phone: String?,
	@SerializedName("country_code") var countryCode: String?,
	var image: String?,
	var token: String?,
)

data class ItemStatsInAdvDetails(
	@SerializedName("type") var stringValueOfType: String?,
	var name: String?,
	@SerializedName("total_count") var totalCount: Int?,
	@SerializedName("previous_month_count") var previousMonthCount: Int?,
	@SerializedName("current_month_count") var currentMonthCount: Int?,
) {
	val growthRate: BigDecimal get() = kotlin.runCatching {
		(currentMonthCount.orZero().toBigDecimal() - previousMonthCount.orZero().toBigDecimal())
			.divide(previousMonthCount.orZero().toBigDecimal(), 2, RoundingMode.HALF_UP)
			.setScale(2, RoundingMode.HALF_UP)
	}.getOrElse { 0.toBigDecimal().setScale(2, RoundingMode.HALF_UP) }

	val type get() = Type.values().firstOrNull { it.apiValue == stringValueOfType }

	enum class Type(val apiValue: String) {
		VIEWS("views"),
		SHARES("shares"),
		CALLS("calls"),
		CHATS("chats"),
		WHATSAPP("whatsapp"),
		FAVORITES("favorites"),
		SEARCH("search"),
		REPORTS("reports"),
	}
}
