package grand.app.moon.domain.shop

import com.google.gson.annotations.SerializedName

/**
 * @param date Ex. 2023-01-18
 */
data class ResponseClientReviews(
	var rate: Int?,
	var review: String?,
	var date: String?,
	var user: ItemUserInClientReviews?,
)

data class ItemUserInClientReviews(
	var id: Int?,
	var verified: Int?,
	@SerializedName("is_shop") var isShop: Boolean?,
	var name: String?,
	var email: String?,
	var phone: String?,
	@SerializedName("country_code") var countryCode: String?,
	var image: String?,
)
/*
{
                "user": {
                    "id": 9785,
                    "verified": 1,
                    "is_shop": false,
                    "name": "Emma",
                    "email": "emma@gmail.com",
                    "phone": "1205577041",
                    "country_code": "+20",
                    "image": "https://sooqmoon.net/storage/users/1674059028r0S5k.webp",
                    "token": null
                },
                "rate": 5,
                "review": "best store ever",
                "date": ""
            }
 */
