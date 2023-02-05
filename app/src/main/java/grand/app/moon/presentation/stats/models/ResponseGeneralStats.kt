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
	val count get() = maxOfMA(totalViews, totalShares, totalCalls, totalWhatsapp)
	// todo na2es el search + chats + store so many stats
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

/*
{
    "code": 200,
    "message": "success",
    "data": {
        "users": {
            "data": [
                {
                    "id": 9797,
                    "verified": 1,
                    "total_views": 3,
                    "is_shop": false,
                    "name": "emma",
                    "email": "emma@test.com",
                    "phone": "1205577044",
                    "country_code": "+20",
                    "image": "https://sooqmoon.net/storage/users/1674465665AqczN.webp",
                    "token": null
                }
            ],
            "links": {
                "first": "http://sooqmoon.net/api/v1/my-advertisements/statistics/37797?page=1",
                "last": "http://sooqmoon.net/api/v1/my-advertisements/statistics/37797?page=1",
                "prev": null,
                "next": null
            },
            "meta": {
                "current_page": 1,
                "from": 1,
                "last_page": 1,
                "links": [
                    {
                        "url": null,
                        "label": "pagination.previous",
                        "active": false
                    },
                    {
                        "url": "http://sooqmoon.net/api/v1/my-advertisements/statistics/37797?page=1",
                        "label": "1",
                        "active": true
                    },
                    {
                        "url": null,
                        "label": "pagination.next",
                        "active": false
                    }
                ],
                "path": "http://sooqmoon.net/api/v1/my-advertisements/statistics/37797",
                "per_page": 15,
                "to": 1,
                "total": 1
            }
        }
    }
}
 */
