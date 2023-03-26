package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.domain.home.models.Store
import grand.app.moon.extensions.AppConsts
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.helpers.paging.BasePaging

fun ItemHomeExplore.toExplore(): Explore {
	return Explore(
		id.orZero(),
		createdAt.orEmpty(),
		files,
		mimeType.orEmpty(),
		likesCount.orZero(),
		sharesCount.orZero(),
		commentsCount.orZero(),
		isLiked.orFalse(),
		Store(
			id = store?.id,
			image = store?.logo,
			name = store?.name,
			share = store?.shareLink,
			isFollowing = store?.isFollowing,
			premium = store?.premium,
		)
	)
}

data class ItemHomeExplore(
	var id: Int?,
	@SerializedName("mime_type") var mimeType: String?,
	@SerializedName("file") var files: List<String>?,
	@SerializedName("likes_count") var likesCount: Int?,
	@SerializedName("comments_count") var commentsCount: Int?,

	@SerializedName("shares_count") var sharesCount: Int?,
	@SerializedName("is_liked") var isLiked: Boolean?,
	@SerializedName("created_at") var createdAt: String?,
	var store: ItemStoreInHomeExplore?,
	@SerializedName("share_link") var shareLink: String?,
) /*: BasePaging.Listener*/ {
	val isVideo get() = mimeType?.contains(AppConsts.Files.MIME_TYPE_PREFIX_VIDEO).orFalse()

	/*@JvmField
	var page: Int? = null

	override fun setPage(page: Int) {
		this.page = page
	}*/
}

data class ItemStoreInHomeExplore(
	var id: Int?,
	@SerializedName("image") var logo: String?,
	var name: String?,
	@SerializedName("share_link") var shareLink: String?,
	@SerializedName("is_following") var isFollowing: Boolean?,
	var premium: Int?,
	@SerializedName("has_offer") var hasOffer: Boolean?,
	var stories: List<ResponseStory.Story>?,
	var phone: String?,
	var createdAt: String?,
	var country: ItemAdvertisementInResponseHome.Country?,
	var nickname: String?,
	@SerializedName("ads_phone") var adsPhone: String?,
	@SerializedName("whatsapp_phone") var whatsappPhone: String?,
	@SerializedName("background_image") var backgroundImage: String?,
	@SerializedName("average_rate") var averageRate: Float?,
	@SerializedName("advertisements_count") var advertisementsCount: Int?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("ads_country_code") var adsCountryCode: String?,
	@SerializedName("whatsapp_country_code") var whatsappCountryCode: String?,
	//var image: String?,
) : ItemInResponseHome {
	val isPremium get() = premium == 1
}

/*
                "store": {
                    "share_link": "http://eg.sooqmoon.net/en/shop/9811/beko000000",
                    "id": 9811,
                    "name": "bakry store",
                    "has_offer": false,
                    "nickname": "beko000000",
                    "email": "medobakry0000@gmail.com",
                    "phone": "555555",
                    "country_code": "+20",
                    "website": "www.google.com",
                    "latitude": 30.103349857202,
                    "longitude": 31.374175027013,
                    "token": null,
                    "average_rate": 0,
                    "rate_count": 0,
                    "premium": 0,
                    "advertisements_count": 0,
                    "followers_count": 0,
                    "views_count": 0,
                    "is_following": false,
                    "image": "https://sooqmoon.net/storage/users/1675004633WfYaq.webp",
                    "background_image": "https://sooqmoon.net/storage/users/1675004633n3F4h.webp",
                    "social_media_links": null,
                    "working_hours": [
                        {
                            "from": null,
                            "to": null,
                            "status": true
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": false
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": false
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": false
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": false
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": false
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": false
                        }
                    ],
                    "description": "bakry store",
                    "created_at": "1 week ago"
                },
                "": "2023 January 30"
            },
 */
