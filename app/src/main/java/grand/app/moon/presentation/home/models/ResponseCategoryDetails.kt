package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName

data class ResponseCategoryDetails(
	@SerializedName("ads_count") var adsCount: Int?,
	var stores: List<ItemStoreInResponseHome>?,
	var advertisements: List<ItemAdvertisementInResponseHome>?,
)

/*
ItemStoreInResponseHome>?,
	@SerializedName("followings-stores") var followingsStores: ,
	@SerializedName("suggested-ads") var suggestedAds: List<ItemAdvertisementInResponseHome>?,
	@SerializedName("most-popular-ads") var mostPopularAds: List<ItemAdvertisementInResponseHome>?,
 */
/*
"": 103,
        ""
 */
