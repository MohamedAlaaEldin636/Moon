package grand.app.moon.presentation.stats.models

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import grand.app.moon.R
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.myAds.model.ResponseMyAdvDetails
import java.math.BigDecimal
import java.math.RoundingMode

data class ResponseStoreStats(
	var statistics: List<ItemStoreStats>?,
	@SerializedName("recent_premium_ads") var recentPremiumAds: List<ResponseMyAdvDetails>?,
	@SerializedName("most_viewed_ads") var mostViewedAds: List<ResponseMyAdvDetails>?,
	@SerializedName("recently_added_ads") var recentlyAddedAds: List<ResponseMyAdvDetails>?,
)

data class ItemStoreStats(
	@SerializedName("type") var statsType: String?,
	var name: String?,
	@SerializedName("total_count") var totalCount: Int?,
	@SerializedName("previous_month_count") var previousMonthCount: Int?,
	@SerializedName("current_month_count") var currentMonthCount: Int?,
	var chart: ResponseGeneralStats,
) {

	val growthRate: BigDecimal get() = kotlin.runCatching {
		(currentMonthCount.orZero().toBigDecimal() - previousMonthCount.orZero().toBigDecimal())
			.divide(previousMonthCount.orZero().toBigDecimal(), 2, RoundingMode.HALF_UP)
			.setScale(2, RoundingMode.HALF_UP)
	}.getOrElse { 0.toBigDecimal().setScale(2, RoundingMode.HALF_UP) }

	val type get() = Type.values().firstOrNull { it.apiValue == statsType }

	enum class Type(val apiValue: String, @StringRes val titlePluralRes: Int, @StringRes val titleSingularRes: Int) {
		FOLLLOWERS("followers", R.string.followers_2, R.string.follower),
		ADVERTISEMENTS("advertisements", R.string.advertisements_8, R.string.advertisement),
		STORIES("stories", R.string.stories_8, R.string.story),
		SHARES("shares", R.string.store_shares, R.string.share),
		CALLS("calls", R.string.calls, R.string.call_3),
		CHATS("chats", R.string.conversation, R.string.chat_9),
		WHATSAPP("whatsapps"/*s*/, R.string.whatsapp_calls, R.string.whatsapp_call),
		REPORTS("reports", R.string.reports_store, R.string.report_1),
		BLOCKS("blocks", R.string.store_block, R.string.block),
		EXPLORES("explores", R.string.explore_files, R.string.file_2),
		VIEWS("views", R.string.store_views, R.string.view_921),
		AD_SHARES("ad_shares", R.string.shares_ad, R.string.share),
		AD_VIEWS("ad_views", R.string.ad_views, R.string.view_921),
		AD_REPORTS("ad_reports", R.string.reports_ads, R.string.report_1),
		AD_SEARCHES("ad_searches", R.string.search_about_an_advertisements, R.string.search),
		AD_FAVORITES("ad_favorites", R.string.fav_ads, R.string.advertisement_66),
	}

}
