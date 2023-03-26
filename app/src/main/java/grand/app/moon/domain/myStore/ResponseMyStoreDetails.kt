package grand.app.moon.domain.myStore

import com.google.gson.annotations.SerializedName

data class ResponseSuccessPackageForBecomeShop(
	@SerializedName("store_info") var storeInfoIsCompleted: Boolean?,
)

/**
 * @param id used in response to store info api not in request of creation or update it.
 */
data class ResponseMyStoreDetails(
	var id: Int?,
	@SerializedName("background_image") var coverImage: String?,
	@SerializedName("image") var logoImage: String?,
	@SerializedName("name") var storeName: String?,
	@SerializedName("nickname") var userName: String?,
	@SerializedName("city_id") var cityId: Int?,
	@SerializedName("area_id") var areaId: Int?,
	var latitude: String?,
	var longitude: String?,
	var address: String?,
	var description: String?,
	@SerializedName("advertising_website") var advertisingWebsite: String?,
	var email: String?,
	@SerializedName("website") var websiteLink: String?,
	@SerializedName("ads_phone") var adsPhone: String?,
	@SerializedName("whatsapp_phone") var whatsappPhone: String?,
	@SerializedName("tax_number") var taxNumber: String?,

	var phone: String?,
	@SerializedName("country_code") var countryCode: String?,
	@SerializedName("progress_bar") var progressBar: Int?,
	@SerializedName("store_info_status") var storeInfo: Boolean?,
	@SerializedName("categories_status") var categories: Boolean?,
	@SerializedName("sub_categories_status") var subCategories: Boolean?,
	@SerializedName("working_hours_status") var workingHours: Boolean?,
	@SerializedName("social_media_links_status") var socialMediaLinks: Boolean?,

	@SerializedName("ads_country_code") var adsCountryCode: String?,
	@SerializedName("whatsapp_country_code") var whatsappCountryCode: String?,
)
/*
"store_info_status":false,
"categories_status":false,
"sub_categories_status":false,
"working_hours_status":false
,"social_media_links_status":false,

 */
