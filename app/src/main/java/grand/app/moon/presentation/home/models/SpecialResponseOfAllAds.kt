package grand.app.moon.presentation.home.models

import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.helpers.paging.map

data class SpecialResponseOfAllAds(
	var response1: MABaseResponse<ResponseAllAdsOfCategory>? = null,
	var response2: MABaseResponse<MABasePaging<ItemAdvertisementInResponseHome>>? = null,
) {
	fun toPaging(): MABaseResponse<MABasePaging<ItemAdvertisementInResponseHome>>? {
		response2?.also {
			return it
		}

		response1?.also { maBaseResponse ->
			return maBaseResponse.map {
				it?.advertisements
			}
		}

		return null
	}
}

fun MABaseResponse<ResponseAllAdsOfCategory>?.toSpecialResponseOfAllAds1() = SpecialResponseOfAllAds(
	response1 = this
)

fun MABaseResponse<MABasePaging<ItemAdvertisementInResponseHome>>?.toSpecialResponseOfAllAds2() = SpecialResponseOfAllAds(
	response2 = this
)
