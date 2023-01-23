package grand.app.moon.domain.shop

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import grand.app.moon.R

data class ResponseStoreSocialMedia(
	@SerializedName("type") var linkType: String?,
	@SerializedName("link") var linkUrl: String? = null,
) {

	val typeOfLink get() = Type.values().firstOrNull { it.apiType == linkType };

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

}
