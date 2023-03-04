package grand.app.moon.presentation.myAds.model

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.extensions.orZero
import java.math.BigDecimal
import java.math.RoundingMode
import grand.app.moon.R
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome

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
	var price: Float?,
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

	var store: ItemAdvertisementInResponseHome.Store?,
	@SerializedName("similar_ads") var similarAds: List<ItemAdvertisementInResponseHome>?,
	@SerializedName("similar_stores") var similarStores: List<ItemStoreInResponseHome>?,
) {
	val isPremium get() = premium == 1

	val isNegotiable get() = negotiable == 1

	val fullPhone get() = "${country?.countryCode.orEmpty()}${phone.orEmpty()}"

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

	/*
	'followers','advertisements','stories','shares','calls','chats','whatsapps','reports','blocks','explores','views','ad_shares','ad_views','ad_reports', 'ad_searches'
	 */
	enum class Type(val apiValue: String, @StringRes val titlePluralRes: Int, @StringRes val titleSingularRes: Int) {
		VIEWS("views", R.string.view_ad, R.string.view_921),
		SHARES("shares", R.string.share_ad, R.string.share),
		CALLS("calls", R.string.calls, R.string.call_3),
		CHATS("chats", R.string.conversation, R.string.chat_9),
		WHATSAPP("whatsapp", R.string.whatsapp_calls, R.string.whatsapp_call),
		FAVORITES("favorites", R.string.favourite, R.string.favourite_8),
		SEARCH("search", R.string.search_about_an_adv, R.string.search),
		REPORTS("reports", R.string.reports_1, R.string.report_1),
	}
}
