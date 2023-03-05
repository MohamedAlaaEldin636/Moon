package grand.app.moon.domain.shop

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import grand.app.moon.R

data class ResponseStoreSocialMedia(
	@SerializedName("type") var linkType: String?,
	@SerializedName("link") var linkUrl: String? = null,
) {

	val typeOfLink get() = Type.values().firstOrNull { it.apiType == linkType }

	val storeDetailsType get() = StoreDetailsType.values().firstOrNull { it.apiType == linkType }

	enum class Type(
		val apiType: String,
		@StringRes val stringRes: Int,
		@DrawableRes val drawableRes: Int
	) {
		FACEBOOK("facebook", R.string.facebook, R.drawable.ic_facebook_9),
		INSTAGRAM("instagram", R.string.instagram, R.drawable.ic_instagram_9),
		TWITTER("twitter", R.string.twitter, R.drawable.ic_twitter_9),
		YOUTUBE("youtube", R.string.youtube, R.drawable.ic_youtube_9),
		SNAPCHAT("snapchat", R.string.snapchat, R.drawable.ic_snapchat_9),
	}

	companion object {
		const val STORE_DETAILS_ADVERTISING_WEBSITE_API_TYPE = "STORE_DETAILS_ADVERTISING_WEBSITE_API_TYPE"
		const val STORE_DETAILS_COPY_lINK_API_TYPE = "STORE_DETAILS_COPY_lINK_API_TYPE"
	}

	enum class StoreDetailsType(
		val apiType: String,
		@StringRes val stringRes: Int,
		@DrawableRes val drawableRes: Int
	) {
		FACEBOOK("facebook", R.string.facebook_7989, R.drawable.adv_details_facebook),
		INSTAGRAM("instagram", R.string.instagram, R.drawable.adv_details_instagram),
		TWITTER("twitter", R.string.twitter, R.drawable.adv_details_twitter),
		YOUTUBE("youtube", R.string.youtube, R.drawable.adv_details_youtube),
		SNAPCHAT("snapchat", R.string.snapchat, R.drawable.store_details_snapchat_with_container),
		ADVERTISING_WEBSITE(STORE_DETAILS_ADVERTISING_WEBSITE_API_TYPE, R.string.location_99, R.drawable.store_details_website_with_container),
		COPY_LINK(STORE_DETAILS_COPY_lINK_API_TYPE, R.string.copy_link, R.drawable.store_details_copy_link_with_container),
	}

}
