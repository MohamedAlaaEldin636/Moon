package grand.app.moon.presentation.home.models

data class ResponseChatAgent(
	var uid: String?,
	var name: String?,
	var avatar: String?,
	var status: String?,
	var role: String?,
	var lastActiveAt: Long?,
	var createdAt: Long?,
	var updatedAt: Long?,
)
/*
"data": {
        "uid": "admin_1",
        "name": "Badr",
        "avatar": "https://sooqmoon.net/storage/settings/1652708862N2yMX.webp",
        "status": "available",
        "role": "moon_admins",
        "lastActiveAt": 1678006777,
        "createdAt": 1677588721,
        "updatedAt": 1677590372
    }
 */
