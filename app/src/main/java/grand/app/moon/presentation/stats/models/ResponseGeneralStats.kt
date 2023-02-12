package grand.app.moon.presentation.stats.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.extensions.orZero
import grand.app.moon.helpers.paging.MABasePaging
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max

// ItemStatsInAdvDetails.Type

data class ResponseGeneralStats(
	@SerializedName("current_week") var currentWeek: List<Int>?,
	@SerializedName("previous_week") var previousWeek: List<Int>?,
	var users: MABasePaging<ResponseUserInGeneralStats>?,
) {
	private val currentMonthTotalCount get() = currentWeek.orEmpty().sumOf { it }
	private val previousMonthTotalCount get() = previousWeek.orEmpty().sumOf { it }

	val growthRateCurrentWeek: BigDecimal get() = kotlin.runCatching {
		(currentMonthTotalCount.orZero().toBigDecimal() - previousMonthTotalCount.orZero().toBigDecimal())
			.divide(previousMonthTotalCount.orZero().toBigDecimal(), 2, RoundingMode.HALF_UP)
			.setScale(2, RoundingMode.HALF_UP)
	}.getOrElse { 0.toBigDecimal().setScale(2, RoundingMode.HALF_UP) }

	val growthRatePreviousWeek: BigDecimal get() = kotlin.runCatching {
		(previousMonthTotalCount.orZero().toBigDecimal() - currentMonthTotalCount.orZero().toBigDecimal())
			.divide(currentMonthTotalCount.orZero().toBigDecimal(), 2, RoundingMode.HALF_UP)
			.setScale(2, RoundingMode.HALF_UP)
	}.getOrElse { 0.toBigDecimal().setScale(2, RoundingMode.HALF_UP) }
}

data class ResponseUserInGeneralStats(
	@SerializedName("total_views") var totalViews: Int?,
	@SerializedName("total_shares") var totalShares: Int?,
	@SerializedName("total_calls") var totalCalls: Int?,
	@SerializedName("total_whatsapp") var totalWhatsapp: Int?,
	@SerializedName("total_whatsapps") var totalWhatsapps: Int?,
	@SerializedName("total_chats") var totalChats: Int?,
	//@SerializedName("total_favorites") var totalFavorites: Int?,
	@SerializedName("total_search") var totalSearch: Int?,
	@SerializedName("total_reports") var totalReports: Int?,
	@SerializedName("total_blocks") var totalBlocks: Int?,
	@SerializedName("total_ad_shares") var totalAdShares: Int?,
	@SerializedName("total_ad_views") var totalAdViews: Int?,
	@SerializedName("total_ad_reports") var totalAdReports: Int?,
	@SerializedName("total_ad_searches") var totalAdSearches: Int?,
	//@SerializedName("total_ad_favorites") var totalReports: Int?,

	var id: Int?,
	var verified: Int?,
	@SerializedName("is_shop") var isShop: Boolean?,
	var name: String?,
	var email: String?,
	var phone: String?,
	@SerializedName("country_code") var countryCode: String?,
	var image: String?,
	@SerializedName("created_at") var createdAt: String?,
) {
	val count get() = maxOfMA(
		totalViews, totalShares, totalCalls, totalWhatsapp, totalChats, totalSearch, totalReports, totalWhatsapps,
		totalBlocks, totalAdShares, totalAdViews, totalAdReports, totalAdSearches
	)
}

fun maxOfMA(vararg values: Int?): Int? {
	var max: Int? = null
	for (item in values) {
		if (item != null && (max == null || item > max)) {
			max = item
		}
	}
	return max
}
