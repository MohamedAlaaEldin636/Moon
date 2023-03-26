package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.trimFirstPlusIfExistsOrEmpty

fun  ItemAdvertisementInResponseHome.Store.toItemStoreInResponseHome(): ItemStoreInResponseHome {
	return ItemStoreInResponseHome(
		id, image, name, nickname, averageRate, viewsCount, advertisementsCount,
		isFollowing, premium, hasOffer, stories, phone, createdAt, country, backgroundImage, adsPhone, whatsappPhone,
		adsCountryCode, whatsappCountryCode
	)
}

fun ResponseSearchResult.toItemStoreInResponseHome(): ItemStoreInResponseHome {
	return ItemStoreInResponseHome(
		id, image, name, nickname, averageRate, viewsCount, advertisementsCount,
		isFollowing, premium, hasOffer, stories, phone, createdAt, country, backgroundImage, adsPhone, whatsappPhone,
		adsCountryCode, whatsappCountryCode
	)
}

fun ItemStoreInHomeExplore.toItemStoreInResponseHome(): ItemStoreInResponseHome {
	return ItemStoreInResponseHome(
		id, logo, name, nickname, averageRate, viewsCount, advertisementsCount,
		isFollowing, premium, hasOffer, stories, phone, createdAt, country, backgroundImage, adsPhone, whatsappPhone,
		adsCountryCode, whatsappCountryCode
	)
}

fun ResponseStoreDetails.toItemStoreInResponseHome(): ItemStoreInResponseHome {
	return ItemStoreInResponseHome(
		id, image, name, nickname, averageRate, viewsCount, advertisementsCount, isFollowing, premium, hasOffer, stories, phone, createdAt, country, backgroundImage,
		adsPhone, whatsappPhone,
		adsCountryCode, whatsappCountryCode
	)
}

data class ItemHomeRV(
	val type: Type,
	val name: String?,
	val count: Int? = null,
	val dynamicCategoriesAdsId: Int? = null,
) {
	enum class Type(val apiValue: String) {
		STORIES("stories"),
		CATEGORIES("categories"),
		MOST_RATED_STORIES("most-rated-stores"),
		FOLLOWING_STORIES("followings-stores"),
		SUGGESTED_ADS("suggested-ads"),
		MOST_POPULAR_ADS("most-popular-ads"),
		DYNAMIC_CATEGORIES_ADS("category-ads"),
	}
}

sealed interface ItemInResponseHome

data class ItemStoreInResponseHome(
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
	@SerializedName("background_image") var backgroundImage: String?,
	@SerializedName("ads_phone") var adsPhone: String?,
	@SerializedName("whatsapp_phone") var whatsappPhone: String?,
	@SerializedName("ads_country_code") var adsCountryCode: String?,
	@SerializedName("whatsapp_country_code") var whatsappCountryCode: String?,
) : ItemInResponseHome {
	val isPremium get() = premium == 1
}

/*
{
                "id": 23091,
                "title": "منخل معدني",
                "store_id": 9739,
                "price": 2.8,
                "premium": 1,
                "is_negotiable": 0,
                "created_at": "6 months ago",
                "date": 1657125233000,
                "views_count": 9,
                "favorite_count": 0,
                "share_count": 1,
                "latitude": 24.69,
                "longitude": 46.72,
                "phone": "1205577043",
                "sub_category_id": 437,
                "category_id": 431,
                "category": {
                    "id": 431,
                    "name": "home and kitchen",
                    "image": "https://sooqmoon.net/storage/categories/1660650050eU8is.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                "is_favorite": false,
                "average_rate": 1,
                "image": "https://sooqmoon.net/storage/advertisements/1657125233Evw4Z.webp",
                "store": {
                    "share_link": "http://eg.sooqmoon.net/en/shop/9739/emma-store",
                    "id": 9739,
                    "name": "Emma Store",
                    "has_offer": true,
                    "nickname": "emma_store",
                    "email": "emma@gmail.com",
                    "phone": "1205577043",
                    "country_code": "+20",
                    "website": "www.emma.com",
                    "latitude": 30.14367614261,
                    "longitude": 31.398016651865,
                    "token": null,
                    "average_rate": 5,
                    "rate_count": 1,
                    "premium": 1,
                    "advertisements_count": 0,
                    "followers_count": 0,
                    "views_count": 0,
                    "is_following": false,
                    "image": "https://sooqmoon.net/storage/users/1673954257TzD9r.webp",
                    "background_image": "https://sooqmoon.net/storage/users/1674393758Ic7L5.webp",
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
                    "created_at": "4 weeks ago"
                },
                "country": {
                    "id": 2,
                    "name": "Sultanate of Oman",
                    "currency": "OMR",
                    "country_code": "+968",
                    "iso_code": "OM",
                    "image": "https://sooqmoon.net/storage/countries/1649929925GqoKL.webp"
                },
                "city": {
                    "id": 13,
                    "name": "Al Buraimi",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1657015177ibQgk.webp"
                }
            },
 */
/*fun ItemAdvertisementInResponseHome.toResponseMyAdvDetails(): ResponseMyAdvDetails {
	return ResponseMyAdvDetails(
		id,
		title,
		description,
		latitude,
		longitude,
		address,
		price,
		premium,
		createdAt,

	)
}*/

data class ItemAdvertisementInResponseHome(
	var id: Int?,
	@SerializedName("is_favorite") var isFavorite: Boolean?,
	var premium: Int?,
	var image: String?,
	@SerializedName("favorite_count") var favoriteCount: Int?,
	@SerializedName("views_count") var viewsCount: Int?,
	@SerializedName("average_rate") var averageRate: Float?,
	var title: String?,
	@SerializedName("created_at") var createdAt: String?,
	var country: Country?, // name
	var city: City?, // name
	var store: Store?, // stories, id, image, nicname
	var price: Float?,
	@SerializedName("is_negotiable") var negotiable: Int?,
	var phone: String?,

	// used for paging not data from backend
	@SerializedName("ads_count") var adsCount: Int?,
	var slider: List<ItemAdvertisementInResponseHome>?,

	@SerializedName("store_category_id") var storeCategoryId: Int?,
	@SerializedName("store_sub_category_id") var storeSubCategoryId: Int?,
	@SerializedName("sub_category_id") var subCategoryId: Int?,
	@SerializedName("brand_id") var brandId: Int?,
	var category: ItemCategory?,
	@SerializedName("date") var dateInMs: Long?,
	//@SerializedName("sub_category_id") var subCategoryId: Int?,
	var area: Area?, // name
) : ItemInResponseHome {

	val fullPhone get() = "${country?.countryCode.orEmpty()}${phone.orEmpty()}"

	val isNegotiable get() = negotiable == 1

	val isPremium get() = premium == 1

	fun makePremium() {
		premium = 1
	}

	data class Country(
		var id: Int?,
		var name: String?,
		var currency: String?,
		@SerializedName("country_code") var countryCode: String?,
		var image: String?,
	)

	data class City(
		var id: Int?,
		var name: String?,
	)

	data class Area(
		var id: Int?,
		var name: String?,
	)

	data class Store(
		var id: Int?,
		var image: String?,
		var name: String?,
		var nickname: String?,
		var stories: List<ResponseStory.Story>?,
		var phone: String?,
		var createdAt: String?,
		var country: Country?,
		@SerializedName("is_following") var isFollowing: Boolean?,
		@SerializedName("average_rate") var averageRate: Float?,
		@SerializedName("ads_phone") var adsPhone: String?,
		@SerializedName("whatsapp_phone") var whatsappPhone: String?,
		@SerializedName("background_image") var backgroundImage: String?,
		@SerializedName("advertisements_count") var advertisementsCount: Int?,
		@SerializedName("views_count") var viewsCount: Int?,
		@SerializedName("has_offer") var hasOffer: Boolean?,
		var premium: Int?,
		@SerializedName("ads_country_code") var adsCountryCode: String?,
		@SerializedName("whatsapp_country_code") var whatsappCountryCode: String?,
) {
		val fullWhatsAppPhone get() = if (whatsappPhone.isNullOrEmpty()) fullPhone else {
			val countryCode = whatsappCountryCode.orEmpty().trimFirstPlusIfExistsOrEmpty()
			"$countryCode${whatsappPhone.orEmpty()}"
		}
		val fullAdsPhone get() = if (adsPhone.isNullOrEmpty()) fullPhone else {
			val countryCode = adsCountryCode.orEmpty().trimFirstPlusIfExistsOrEmpty()
			"$countryCode${adsPhone.orEmpty()}"
		}
		val fullPhone get() = if (phone.isNullOrEmpty()) "" else "${country?.countryCode.orEmpty()}${phone.orEmpty()}"
 	}
}

data class ResponseHome(
	@SerializedName("most-rated-stores") var mostRatedStores: List<ItemStoreInResponseHome>?,
	@SerializedName("followings-stores") var followingsStores: List<ItemStoreInResponseHome>?,
	@SerializedName("suggested-ads") var suggestedAds: List<ItemAdvertisementInResponseHome>?,
	@SerializedName("most-popular-ads") var mostPopularAds: List<ItemAdvertisementInResponseHome>?,
	@SerializedName("category-ads") var dynamicCategoriesAds: List<Category>?,
	@SerializedName("notification_count") var notificationCount: Int?,
) {

	data class Category(
		var id: Int?,
		var name: String?,
		var image: String?,
		@SerializedName("ads_count") var adsCount: Int?,
		@SerializedName("order_by_no") var orderByNo: Int?,
		var advertisements: List<ItemAdvertisementInResponseHome>?,
	)

}
