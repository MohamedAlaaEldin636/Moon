package grand.app.moon.domain.packages

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import grand.app.moon.R
import grand.app.moon.extensions.orZero
import kotlin.time.Duration.Companion.days

data class BasePagination<T>(
	var data: List<T>?,
	var links: PaginationLinks?,
	var meta: PaginationMeta?,
)

data class PaginationLinks(
	var first: String?,
	var last: String?,
	var prev: String?,
	var next: String?,
)

data class PaginationMeta(
	@SerializedName("per_page") var perPage: Int?,
	@SerializedName("current_page") var currentPage: Int?,
) {
	val oneThirdPerPage get() = perPage.orZero() / 3
}

// premium_advertisements=>1,premium_shops=>2,shops=>0
enum class PackageType(val apiValue: String) {
	PREMIUM_ADS("premium_advertisements"),
	PREMIUM_SHOPS("premium_shops"),
	BECOME_SHOP("shops"),
}

// period_type =>{0=>days,1=>month,2=>year,3=>free}
// recommended=>{0=>flase,1=>true}
data class ResponsePackage(
	var id: Int?,
	var period: Int?,
	@SerializedName("period_type") var periodType: Int?,
	var recommended: Int?,
	@SerializedName("advertisements_count") var advertisementsCount: Int?,
	var price: Float?,
	@SerializedName("price_before") var priceBefore: Int?,
	@SerializedName("stories_count") var storiesCount: Int?,
	@SerializedName("explores_count") var exploresCount: Int?,
	@SerializedName("statistics_views_count") var statisticsViewsCount: Int?,
	var title: String?,
	@SerializedName("advertisements_tooltip") var advertisementsTooltip: String?,
	@SerializedName("stories_tooltip") var storiesTooltip: String?,
	@SerializedName("explores_tooltip") var exploresTooltip: String?,
	@SerializedName("statistics_tooltip") var statisticsTooltip: String?,
	@SerializedName("is_subscribed") var isSubscribed: Boolean?,
	var country: ItemBaseCountry?,
	@SerializedName("rest_days") var restDays: Int?,
) {

	val typePeriod get() = TypePeriod.values().firstOrNull { it.apiValue == periodType }

	val isPopular get() = recommended == 1

	enum class TypePeriod(val apiValue: Int, @StringRes val stringRes: Int) {
		DAYS(0, R.string.days),
		MONTHS(1, R.string.months),
		YEARS(2, R.string.years),
		FREE(3, R.string.free),
	}

	fun getPeriodInDays(): Int {
		val period = period.orZero()
		return when (typePeriod) {
			TypePeriod.DAYS, TypePeriod.FREE, null -> period
			TypePeriod.MONTHS -> period * 30
			TypePeriod.YEARS -> period * 365
		}
	}

}

data class ItemBaseCountry(
	var id: Int?,
	var name: String?,
	var image: String?,
	var currency: String?,
	@SerializedName("currency_code") var currencyCode: String?,
	@SerializedName("iso_code") var isoCode: String?,
)
