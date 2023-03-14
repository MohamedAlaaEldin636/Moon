package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.toArrayList
import grand.app.moon.presentation.base.utils.Constants

/**
 * @param image is logo used
 * @param stories -> [file] is full image/video known by [mime_type] & [share_link] used with
 * [is_liked] & [story_link_type] to know if whatsapp/call/chat
 * @param name used with [nickname] & [created_at]
 */
data class ResponseStory(
	@SerializedName("share_link") var shareLink: String? = null,
	var id: Int? = null,
	var name: String? = null,
	var nickname: String? = null,
	var email: String? = null,
	var phone: String? = null,
	@SerializedName("country_code") var countryCode: String? = null,
	var website: String? = null,
	var latitude: String? = null,
	var longitude: String? = null,
	@SerializedName("average_rate") var averageRate: Float? = null,
	@SerializedName("rate_count") var rateCount: Int? = null,
	var premium: Int? = null,
	@SerializedName("advertisements_count") var advertisementsCount: Int? = null,
	@SerializedName("followers_count") var followersCount: Int? = null,
	@SerializedName("views_count") var viewsCount: Int? = null,
	@SerializedName("is_following") var isFollowing: Boolean? = null,
	var image: String? = null,
	@SerializedName("background_image") var backgroundImage: String? = null,
	var description: String? = null,
	@SerializedName("created_at") var createdAt: String? = null,
	var stories: List<Story>? = null,
	var isSouqMoonStory: Boolean? = false,
	@SerializedName("highlight_name") var highlightName: String? = null,
	@SerializedName("highlight_cover") var highlightCover: String? = null,
	//@SerializedName("social_media_links") var socialMediaLinks: String?,
	//@SerializedName("working_hours") var workingHours: String?,
	@SerializedName("chat_agent") var chatAgent: ResponseChatAgent? = null,
) {

	val isSeen get() = stories.orEmpty().all { it.isSeen.orFalse() }

	val isPremium get() = premium == 1

	data class Story(
		var id: Int?,
		@SerializedName("mime_type") var mimeType: String?,
		@SerializedName("share_link") var shareLink: String?,
		@SerializedName("story_link_type") var storyLinkType: Int?,
		@SerializedName("is_liked") var isLiked: Boolean?,
		@SerializedName("is_seen") var isSeen: Boolean?,
		var file: String?,
		var store: Store?,
		@SerializedName("highlight_name") var highlightName: String? = null,
		@SerializedName("highlight_cover") var highlightCover: String? = null,
	) {

		val isVideo get() = mimeType?.startsWith(Constants.VIDEO).orFalse()

		val linkType get() = StoryLink.values().firstOrNull { it.apiValue == storyLinkType }

		data class Store(
			var name: String?,
			var image: String?,
			@SerializedName("share_link") var shareLink: String?,
			var id: Int?,
			var phone: String?,
			@SerializedName("country_code") var countryCode: String?,
			var website: String?,
			var latitude: Float?,
			var longitude: Float?,
			@SerializedName("average_rate") var averageRate: Float?,
			@SerializedName("rate_count") var rateCount: Int?,
			var premium: Int?,
			@SerializedName("advertisements_count") var advertisementsCount: Int?,
			@SerializedName("followers_count") var followersCount: Int?,
			@SerializedName("views_count") var viewsCount: Int?,
			@SerializedName("is_following") var isFollowing: Boolean?,
			@SerializedName("background_image") var backgroundImage: String?,
			var description: String?,
			//var token: String?,
			//var social_media_links: Unknown(null)?,
			//var working_hours: Unknown(null)?,
		)
	}
}
/*
            "stories": [
                {
                    "share_link": "http://eg.sooqmoon.net/en/shop/3431/vanilla?story=view",
                    "id": 4,
                    "mime_type": "image/jpeg",
                    "story_link_type": 1,
                    "file": "https://sooqmoon.net/storage/stories/1657197101nKs6g.webp",
                    "created_at": "6 months ago",
                    "is_liked": false,
                    "is_seen": false,
                    "store": {
                        "share_link": "http://eg.sooqmoon.net/en/shop/3431/vanilla",
                        "id": 3431,
                        "name": "Vanilla",
                        "nickname": "Vanilla",
                        "email": "Vanilla@gmail.com",
                        "phone": "0966365247",
                        "country_code": null,
                        "website": null,
                        "latitude": 30.0906522,
                        "longitude": 31.3230963,
                        "token": null,
                        "average_rate": 0,
                        "rate_count": 0,
                        "premium": 0,
                        "advertisements_count": 0,
                        "followers_count": 0,
                        "views_count": 0,
                        "is_following": false,
                        "image": "https://sooqmoon.net/storage/stores/1657123047iXP1J.webp",
                        "background_image": null,
                        "social_media_links": null,
                        "working_hours": null,
                        "description": null,
                        "created_at": "6 months ago"
                    }
                },
 */

fun ResponseStory.toStore(): Store {
	// id of store, name, image, premium, nickname,
	// stories{is_liked, id, seen, isFirst, file, shareLink},
	return Store(
		id = id,
		nickname = nickname,
		name = name,
		image = image,
		premium = premium,
		phone = phone,
		stories = stories?.map { story ->
			StoryItem(
				id = story.id.orZero(),
				is_liked = story.isLiked.orFalse(),
				isSeen = story.isSeen.orFalse(),
				//isFirst = story.isf, // story_link_type // StoryLink
				file = story.file.orEmpty(),
				shareLink = story.shareLink.orEmpty(),
				mimeType = story.mimeType.orEmpty(),
				storyLinkType = story.storyLinkType
			)
		}?.toArrayList()
	)
}
