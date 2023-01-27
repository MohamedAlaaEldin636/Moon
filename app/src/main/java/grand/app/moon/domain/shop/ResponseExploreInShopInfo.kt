package grand.app.moon.domain.shop

import com.google.gson.annotations.SerializedName
import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.presentation.base.utils.Constants

data class ResponseStoriesInShopInfo(
	@SerializedName("stories_rest_count") var storiesRestCount: Int?,
	var stories: MABasePaging<ItemStoryInShopInfo>
)

data class ItemStoryInShopInfo(
	var id: Int?,
	@SerializedName("mime_type") var mimeType: String?,
	@SerializedName("file") var file: String?,
	@SerializedName("likes") var likesCount: Int?,
	@SerializedName("comments_count") var commentsCount: Int?,
	@SerializedName("shares") var sharesCount: Int?,
	@SerializedName("is_liked") var isLiked: Boolean?,
	/** Ex. 2 hours ago */
	@SerializedName("created_at") var createdAtAgo: String?,
	@SerializedName("stories_rest_count") var storiesRestCount: Int?,

	@SerializedName("share_link") var shareLink: String?,
	@SerializedName("story_link_type") var storyLinkType: Int?,
	@SerializedName("is_seen") var isSeen: Boolean?,
	@SerializedName("views") var viewsCount: Int?,
	/** Ex. 24 January 2023 */
	@SerializedName("created_at_date") var createdAtDate: String?,
	@SerializedName("hours") var hoursRemaining: Int?,
	var story: ItemStoreInItemStoryInShopInfo?,
) {
	val isVideo get() = mimeType?.startsWith(Constants.VIDEO) == true

	val type get() = StoryType.values().firstOrNull { it.apiValue == storyLinkType }
}

data class ItemStoreInItemStoryInShopInfo(
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
	@SerializedName("store_info") var storeInfo: Boolean?,
	var categories: Boolean?,
	@SerializedName("sub_categories") var subCategories: Boolean?,
	@SerializedName("working_hours") var workingHours: Boolean?,
	@SerializedName("social_media_links") var socialMediaLinks: Boolean?,

	@SerializedName("share_link") var shareLink: String?,
)
/*
                        "average_rate": 5,
                        "rate_count": 1,
                        "premium": 0,
                        "advertisements_count": 0,
                        "followers_count": 0,
                        "views_count": 0,
                        "is_following": false,
                        "social_media_links": [
                            {
                                "type": "snapchat",
                                "link": "https://facturerapide.com/"
                            },
                            {
                                "type": "facebook",
                                "link": "https://facturerapide.com/"
                            }
                        ],
                        "working_hours": [
                            {
                                "from": "09:20",
                                "to": "21:20",
                                "status": true
                            },
                            {
                                "from": "09:20",
                                "to": "21:20",
                                "status": true
                            },
                            {
                                "from": "09:20",
                                "to": "21:20",
                                "status": true
                            },
                            {
                                "from": "09:20",
                                "to": "21:20",
                                "status": true
                            },
                            {
                                "from": "09:20",
                                "to": "21:20",
                                "status": true
                            },
                            {
                                "from": "09:20",
                                "to": "21:20",
                                "status": true
                            },
                            {
                                "from": "09:20",
                                "to": "21:20",
                                "status": true
                            }
                        ],
                        "description": "emma store is the best",
                        "created_at": "3 weeks ago"
                    },
                },
 */


data class ResponseExploreInShopInfo(
	@SerializedName("explores_rest_count") var exploresRestCount: Int?,
	var explores: MABasePaging<ItemExploreInShopInfo>
)

data class ItemExploreInShopInfo(
	var id: Int?,
	@SerializedName("mime_type") var mimeType: String?,
	@SerializedName("file") var files: List<String>?,
	@SerializedName("likes_count") var likesCount: Int?,
	@SerializedName("comments_count") var commentsCount: Int?,
	@SerializedName("shares_count") var sharesCount: Int?,
	@SerializedName("is_liked") var isLiked: Boolean?,
	@SerializedName("created_at") var createdAt: String?,
	@SerializedName("explores_rest_count") var exploresRestCount: Int?,
) {
	val isVideo get() = mimeType?.startsWith(Constants.VIDEO) == true
}
/*
{
    "code": 200,
    "message": "The action ran successfully!",
    "data": {
        "explores_rest_count": 4,
        "explores": {
            "data": [
                {
                    "id": 99,
                    "mime_type": "video/mp4",
                    "file": [
                        "https://sooqmoon.net/storage/explores/16743966666NPga.mp4",
                        "https://sooqmoon.net/storage/explores/16743966671LZ5S.webp"
                    ],
                    "likes_count": 0,
                    "": 0,
                    "": 0,
                    "": false,
                    "": "2023 January 22"
                },
                {
                    "id": 98,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674396624whzde.webp"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 97,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674396574Fvm3T.webp"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 96,
                    "mime_type": "video/mp4",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674396459Gz57h.mp4"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 95,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674392095tTbs1.webp"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 94,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674058065qEFoi.webp"
                    ],
                    "likes_count": 1,
                    "comments_count": 2,
                    "shares_count": 1,
                    "is_liked": false,
                    "created_at": "2023 January 18"
                }
            ],
 */
