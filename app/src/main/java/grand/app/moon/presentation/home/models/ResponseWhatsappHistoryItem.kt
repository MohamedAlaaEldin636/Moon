package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName

data class ResponseWhatsappHistoryItem(
	var id: Int?,
	var image: String?,
	var name: String?,
	var nickname: String?,
	@SerializedName("average_rate") var averageRate: Float?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("advertisements_count") var advertisementsCount: Int?,
	@SerializedName("is_following") var isFollowing: Boolean?,
	var premium: Int?,
	@SerializedName("has_offer") var hasOffer: Boolean?,
	var stories: List<ResponseStory.Story>?,
	var phone: String?,
	var createdAt: String?,
	var country: ItemAdvertisementInResponseHome.Country?,
	var city: ItemAdvertisementInResponseHome.City?,
	@SerializedName("background_image") var backgroundImage: String?,
	@SerializedName("ads_phone") var adsPhone: String?,
	@SerializedName("whatsapp_phone") var whatsappPhone: String?,
	@SerializedName("ads_country_code") var adsCountryCode: String?,
	@SerializedName("whatsapp_country_code") var whatsappCountryCode: String?,
) {
	val isPremium get() = premium == 1

	val fullWhatsAppPhone get() = if (whatsappPhone.isNullOrEmpty()) fullPhone else {
		val countryCode = whatsappCountryCode.orEmpty()
		"$countryCode${whatsappPhone.orEmpty()}"
	}
	val fullAdsPhone get() = if (adsPhone.isNullOrEmpty()) fullPhone else {
		val countryCode = adsCountryCode.orEmpty()
		"$countryCode${adsPhone.orEmpty()}"
	}
	val fullPhone get() = if (phone.isNullOrEmpty()) "" else "${country?.countryCode.orEmpty()}${phone.orEmpty()}"
}
