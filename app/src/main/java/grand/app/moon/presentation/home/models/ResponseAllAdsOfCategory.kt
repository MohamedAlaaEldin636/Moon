package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.helpers.paging.MABasePaging

data class ResponseAllAdsOfCategory(
	var advertisements: MABasePaging<ItemAdvertisementInResponseHome>?,
	@SerializedName("ads_count") var adsCount: Int?,
	var slider: List<ItemAdvertisementInResponseHome>?
)
