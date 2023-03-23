package grand.app.moon.presentation.home.models

data class ResponseSimpleUserData(
	var id: Int?,
	var name: String?,
	var image: String?,
	var user: ResponseModelUser?,
)

data class ResponseModelUser(
	var id: Int?,
	var name: String?,
	var image: String?,
)

/*
"user": {
                    "id": 9264,
                    "verified": 1,
                    "is_shop": true,
                    "name": "Basem store",
                    "email": "basem@storesi.com",
                    "phone": "1010998759",
                    "country_code": "+20",
                    "image": "https://sooqmoon.net/storage/users/1677595723BwnRl.webp",
                    "token": null,
                    "created_at": null
                }
 */
