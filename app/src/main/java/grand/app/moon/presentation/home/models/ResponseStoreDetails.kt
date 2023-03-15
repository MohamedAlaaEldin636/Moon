package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.domain.shop.ResponseWorkingHour
import grand.app.moon.extensions.orFalse

fun ResponseStoreDetails?.toItemHomeExplore(): ItemStoreInHomeExplore? {
	return ItemStoreInHomeExplore(
		this?.id,
		this?.image,
		this?.name,
		this?.shareLink,
		this?.isFollowing,
		this?.premium,
		this?.hasOffer,
		this?.stories,
		this?.phone,
		this?.createdAt,
		this?.country,
		this?.nickname
	)
}

data class ResponseStoreDetails(
	@SerializedName("share_link") var shareLink: String?,
	var id: Int?,
	var name: String?,
	@SerializedName("has_offer") var hasOffer: Boolean?,
	var nickname: String?,
	var email: String?,
	var phone: String?,
	@SerializedName("country_code") var countryCode: String?,
	var website: String?,
	var latitude: String?,
	var longitude: String?,
	@SerializedName("average_rate") var averageRate: Float?,
	@SerializedName("rate_count") var rateCount: Int?,
	var premium: Int?,
	@SerializedName("advertising_website") var advertisingWebsite: String?,
	@SerializedName("advertisements_count") var advertisementsCount: Int?,
	@SerializedName("followers_count") var followersCount: Int?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("is_following") var isFollowing: Boolean?,
	var image: String?,
	@SerializedName("background_image") var backgroundImage: String?,
	@SerializedName("social_media_links") var socialMediaLinks: List<ResponseStoreSocialMedia>?,
	@SerializedName("working_hours") var workingHours: List<ResponseWorkingHour>?,
	var advertisements: List<ItemAdvertisementInResponseHome>?,
	var description: String?,
	@SerializedName("created_at") var createdAt: String?,
	@SerializedName("category") var categories: List<ItemCategory>?,
	var country: ItemAdvertisementInResponseHome.Country?, // name
	var city: ItemAdvertisementInResponseHome.City?, // name
	var stories: List<ResponseStory.Story>?,
	var highlights: List<ResponseStory.Story>?,
	@SerializedName("store_categories") var storeCategories: List<ItemCategory>?,
	var explores: List<ItemHomeExplore>?,
	@SerializedName("is_store") var isStore: Boolean?,
) {
	val isPremium get() = premium == 1

	val fullPhone get() = "${country?.countryCode.orEmpty()}${phone.orEmpty()}"

	val isSeen get() = stories.orEmpty().all { it.isSeen.orFalse() }
}
