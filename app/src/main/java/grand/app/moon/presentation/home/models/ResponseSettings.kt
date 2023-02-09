package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName

data class ResponseContactUsData(
	var id: Int?,
	var country: String?,
	var emails: List<ItemInsideContactUsData>?,
	var phones: List<ItemInsideContactUsData>?,
	@SerializedName("address") var addresses: List<ItemInsideContactUsData>?,
)

data class ItemInsideContactUsData(
	var id: Int?,
	var title: String?,
	var content: String?,
	var image: String?,
)
/*
{
            "id": 1,
            "country": "Egypt",
            "emails": [
                {
                    "id": 22,
                    "title": null,
                    "content": "Support@souqmoon.com",
                    "image": null
                }
            ],
            "": [
                {
                    "id": 24,
                    "title": null,
                    "content": "0096893274499",
                    "image": null
                }
            ],
            "": [
                {
                    "id": 26,
                    "title": null,
                    "content": "Sultanate of Oman - Muscat - Al Khoud - Mazoon Street.",
                    "image": null
                }
            ]
        },
 */

data class ResponseSettings(
	var id: Int?,
	var content: String?,
	@SerializedName("has_image") var hasImage: Boolean?,
	var title: String?,
	var image: String?,
)
/*
{
            "id": 77,
            "title": null,
            "content": "Technical issue",
            "image": "https://sooqmoon.net/storage/true",
            "has_image": true
        }
 */
